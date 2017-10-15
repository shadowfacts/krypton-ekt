package net.shadowfacts.krypton.ekt.util

import net.shadowfacts.ekt.EKT
import net.shadowfacts.krypton.Page
import java.io.File

class Environment: EKT.TemplateEnvironment {
	override val rootName: String
	override val name: String
	override val cacheDir: File?
	override val data: Map<String, EKT.TypedValue>

	private val page: Page
	private val includesDir: File?

	override val template: String
	override val include: String
		get() = if (includesDir != null) File(includesDir, name).readText(Charsets.UTF_8) else throw RuntimeException("Unable to load include $name, no includes dir specified")

	constructor(page: Page, template: String, cacheDir: File?, includesDir: File?, data: Map<String, EKT.TypedValue>) {
		this.page = page
		this.template = template
		this.rootName = page.source.name
		this.name = rootName
		this.cacheDir = cacheDir
		this.includesDir = includesDir
		this.data = data.toMutableMap().apply {
			put("page", EKT.TypedValue(page, page::class.qualifiedName!!))
		}
	}

	constructor(name: String, parent: Environment, data: Map<String, EKT.TypedValue>?) {
		this.page = parent.page
		this.rootName = parent.rootName
		this.name = name
		this.cacheDir = parent.cacheDir
		this.includesDir = parent.includesDir
		this.data = (data ?: parent.data).toMutableMap().apply {
			put("page", EKT.TypedValue(page, page::class.qualifiedName!!))
		}

		this.template = include
	}

	override fun createChild(name: String, data: Map<String, EKT.TypedValue>?): EKT.TemplateEnvironment {
		return Environment(name, this, data)
	}
}