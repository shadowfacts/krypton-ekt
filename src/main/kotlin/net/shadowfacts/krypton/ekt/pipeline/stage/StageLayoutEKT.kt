package net.shadowfacts.krypton.ekt.pipeline.stage

import net.shadowfacts.ekt.EKT
import net.shadowfacts.krypton.Page
import net.shadowfacts.krypton.ekt.config.ekt
import net.shadowfacts.krypton.ekt.util.EKTException
import net.shadowfacts.krypton.ekt.util.Environment
import net.shadowfacts.krypton.pipeline.stage.Stage
import org.yaml.snakeyaml.Yaml
import java.io.File
import javax.script.ScriptException

/**
 * @author shadowfacts
 */
class StageLayoutEKT(
		private val data: Map<String, EKT.TypedValue> = mapOf()
): Stage() {

	override val id = "layoutEKT"

	constructor(init: EKT.DataProvider.() -> Unit): this (EKT.DataProvider.init(init))

	override fun scan(page: Page) {
	}

	override fun apply(page: Page, input: String): String {
		val cacheDir = page.krypton.config.ekt.cacheDir
		val includesDir = page.krypton.config.ekt.includesDir
		val layoutsDir = page.krypton.config.ekt.layoutsDir

		if (layoutsDir != null && "layout" in page.metadata) {
			val layout = page.metadata["layout"] as String

			if ("pages" in page.metadata) {
				val pages = page.metadata["pages"] as? List<String>
				if (pages != null) {
					page.metadata["pages"] = pages.map {
						layout(page, it, layout, layoutsDir, cacheDir, includesDir)
					}
				}
			} else {
				return layout(page, input, layout, layoutsDir, cacheDir, includesDir)
			}
		}
		return input
	}

	private fun layout(page: Page, content: String, layout: String, layoutsDir: File, cacheDir: File?, includesDir: File?): String {
		val layoutFile = File(layoutsDir, layout)
		val (metadata, layout) = splitYamlFrontmatter(layoutFile)

		val data = data.toMutableMap()
		data["content"] = EKT.TypedValue(content, "String")
		val env = Environment(page, layout, cacheDir, includesDir, data)

		val res = try {
			EKT.render(env)
		} catch (e: ScriptException) {
			throw EKTException("Unable to render layout '$layout' for ${page.source}", e)
		}

		return if ("layout" in metadata) {
			layout(page, res, metadata["layout"] as String, layoutsDir, cacheDir, includesDir)
		} else {
			res
		}
	}

	private fun splitYamlFrontmatter(layout: File): Pair<Map<String, Any>, String> {
		val input = layout.readText(Charsets.UTF_8)
		val parts = input.split("---")
		if (parts.size >= 2) {
			val input = parts.drop(2).joinToString("---")

			val yaml = Yaml().load(parts[1])
			if (System.getProperty("kyrpton.metadata.debugFrontMatter").toBoolean()) {
				println("Front matter for $layout: $yaml")
			}
			return yaml as Map<String, Any> to input
		} else {
			return mapOf<String, Any>() to input
		}
	}

}