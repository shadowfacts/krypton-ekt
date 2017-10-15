package net.shadowfacts.krypton.ekt.pipeline.stage

import net.shadowfacts.ekt.EKT
import net.shadowfacts.krypton.Page
import net.shadowfacts.krypton.ekt.config.ekt
import net.shadowfacts.krypton.ekt.util.Environment
import net.shadowfacts.krypton.pipeline.stage.Stage

/**
 * @author shadowfacts
 */
class StageRenderEKT(
		private val data: Map<String, EKT.TypedValue> = mapOf()
): Stage() {

	override val id = "ekt"

	constructor(init: EKT.DataProvider.() -> Unit): this(EKT.DataProvider.init(init))

	override fun scan(page: Page) {
	}

	override fun apply(page: Page, input: String): String {
		val cacheDir = page.krypton.config.ekt.cacheDir
		val includesDir = page.krypton.config.ekt.includesDir

		val env = Environment(page, input, cacheDir, includesDir, data)
		return EKT.render(env)
	}

}