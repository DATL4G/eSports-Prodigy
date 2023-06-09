package dev.datlag.esports.prodigy.ui.screen.home.info

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import org.kodein.di.DI
import dev.datlag.esports.prodigy.ui.screen.home.info.device.DeviceViewComponent

class InfoViewComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val goToUser: () -> Unit,
    private val goToSettings: () -> Unit
) : InfoComponent, ComponentContext by componentContext {

    override val deviceView = DeviceViewComponent(
        componentContext, di, goToUser, goToSettings
    )

    @Composable
    override fun render() {
        InfoView(this)
    }
}