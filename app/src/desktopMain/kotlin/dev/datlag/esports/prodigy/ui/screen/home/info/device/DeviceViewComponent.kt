package dev.datlag.esports.prodigy.ui.screen.home.info.device

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.overlay.ChildOverlay
import com.arkivanov.decompose.router.overlay.OverlayNavigation
import com.arkivanov.decompose.router.overlay.activate
import com.arkivanov.decompose.router.overlay.childOverlay
import com.arkivanov.decompose.value.Value
import dev.datlag.esports.prodigy.color.createTheme
import dev.datlag.esports.prodigy.common.ioScope
import dev.datlag.esports.prodigy.common.launchIO
import dev.datlag.esports.prodigy.game.HeroicLauncher
import dev.datlag.esports.prodigy.game.SteamLauncher
import dev.datlag.esports.prodigy.game.model.LocalGame
import dev.datlag.esports.prodigy.game.model.LocalGameInfo
import dev.datlag.esports.prodigy.model.common.listFrom
import dev.datlag.esports.prodigy.ui.screen.home.info.device.game.GameConfig
import dev.datlag.esports.prodigy.ui.screen.home.info.device.game.GameViewComponent
import dev.datlag.esports.prodigy.ui.theme.SchemeTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import org.kodein.di.DI
import java.awt.Image

actual class DeviceViewComponent actual constructor(
    componentContext: ComponentContext,
    override val di: DI,
    private val goToUser: () -> Unit,
    private val goToSettings: () -> Unit
) : DeviceComponent, ComponentContext by componentContext {

    private val scope = ioScope()
    private val wantedSteamGameIds = listOf("730", "252950")
    private val wantedHeroicGames = listOf("Rocket League", "Rocket League®")

    private val navigation = OverlayNavigation<GameConfig>()
    private val _child = childOverlay(
        source = navigation,
        initialConfiguration = {
            GameConfig.EMPTY
        },
        handleBackButton = true
    ) { config, componentContext ->
        when (config) {
            is GameConfig.Overview -> GameViewComponent(
                componentContext,
                config.game,
                di
            ) {
                navigation.activate(GameConfig.EMPTY)
            }

            else -> config
        }
    }
    override val child: Value<ChildOverlay<GameConfig, Any>> = _child

    private val steamGames: Flow<List<LocalGameInfo>> = SteamLauncher.appManifests.transform {
        return@transform emit(it.filter { app ->
            wantedSteamGameIds.contains(app.appId)
        }.sortedBy { app -> wantedSteamGameIds.indexOf(app.appId) }.map { app ->
            SteamLauncher.asLocalGameInfo(app)
        })
    }

    private val heroicGames = HeroicLauncher.apps.transform {
        return@transform emit(it.filter { app ->
            wantedHeroicGames.contains(app.title) && app.isInstalled && !app.install.isDLC
        }.map { app ->
            HeroicLauncher.asLocalGameInfo(app)
        })
    }

    override val games: Flow<List<LocalGame>> = combine(steamGames, heroicGames) { steam, heroic ->
        LocalGame.combineGames(listFrom(steam, heroic))
    }

    override fun gameClicked(game: LocalGame) {
        navigation.activate(GameConfig.Overview(game))
    }

    override fun navigateToUser() {
        goToUser()
    }

    override fun navigateToSettings() {
        goToSettings()
    }

    @Composable
    override fun render() {
        DeviceView(this@DeviceViewComponent)
    }
}