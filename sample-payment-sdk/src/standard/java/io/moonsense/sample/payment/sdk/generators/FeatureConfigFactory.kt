package io.moonsense.sample.payment.sdk.generators

import io.moonsense.sdk.features.FeatureGenerator
import io.moonsense.sdk.config.NetworkTelemetryConfig
import io.moonsense.sdk.config.SessionConfig

/**
 * Responsible for returning a [SessionConfig] with [FeatureGenerator]s
 * and a [NetworkTelemetryConfig].
 */
internal object FeatureConfigFactory {

    /**
     * The standard version does not record features or report
     * network telemetry data.
     */
    @Suppress("FunctionOnlyReturningConstant")
    fun getSessionConfig(): SessionConfig? = null
}