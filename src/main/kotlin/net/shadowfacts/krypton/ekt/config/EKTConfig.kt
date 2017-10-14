package net.shadowfacts.krypton.ekt.config

import net.shadowfacts.krypton.config.Configuration
import net.shadowfacts.krypton.config.config
import java.io.File

/**
 * @author shadowfacts
 */
var Configuration.ekt: EKTConfig by config({ throw RuntimeException("EKTConfig cannot be created from String") }, fallback = ::EKTConfig)

class EKTConfig {

	var cacheDir: File? = null
	var includesDir: File? = null
	var layoutsDir: File? = null

}