package net.shadowfacts.krypton.ekt.pipeline.stage

import net.shadowfacts.ekt.EKT
import net.shadowfacts.krypton.Page
import net.shadowfacts.krypton.ekt.config.ekt
import net.shadowfacts.krypton.ekt.util.Environment
import net.shadowfacts.krypton.pipeline.stage.Stage
import java.io.File

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
			val layout = File(layoutsDir, page.metadata["layout"] as String).readText(Charsets.UTF_8)

			if ("pages" in page.metadata) {
				val pages = page.metadata["pages"] as? List<String>
				if (pages != null) {
					page.metadata["pages"] = pages.map {
						layout(page, it, layout, cacheDir, includesDir)
					}
				}
			} else {
				return layout(page, input, layout, cacheDir, includesDir)
			}
		}
		return input
	}

	private fun layout(page: Page, content: String, layout: String, cacheDir: File?, includesDir: File?): String {
		val data = data.toMutableMap()
		data["content"] = EKT.TypedValue(content, "String")
		val env = Environment(page, layout, cacheDir, includesDir, data)
		return EKT.render(env)
	}

}