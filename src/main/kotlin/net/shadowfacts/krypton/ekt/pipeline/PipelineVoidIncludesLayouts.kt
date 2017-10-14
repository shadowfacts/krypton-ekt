package net.shadowfacts.krypton.ekt.pipeline

import net.shadowfacts.krypton.Page
import net.shadowfacts.krypton.ekt.config.ekt
import net.shadowfacts.krypton.pipeline.Pipeline
import net.shadowfacts.krypton.pipeline.selector.PipelineSelector
import java.io.File

/**
 * @author shadowfacts
 */
object PipelineVoidIncludesLayouts: Pipeline(SelectorVoidIncludesLayouts)

object SelectorVoidIncludesLayouts: PipelineSelector {

	override fun select(page: Page, file: File): Boolean {
		val path = page.source.toPath()
		val includesDir = page.krypton.config.ekt.includesDir
		val layoutsDir = page.krypton.config.ekt.layoutsDir
		if (includesDir != null && path.startsWith(includesDir.toPath())) return true
		if (layoutsDir != null && path.startsWith(layoutsDir.toPath())) return true
		return false
	}

}