package dev.datlag.esports.prodigy.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.pages.Pages
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.datlag.esports.prodigy.ui.LocalWindowSize
import dev.datlag.esports.prodigy.ui.WindowSize
import dev.datlag.esports.prodigy.ui.custom.ExpandedPages
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun HomeScreen(component: HomeComponent) {
    when (LocalWindowSize.current) {
        WindowSize.COMPACT -> CompactScreen(component)
        WindowSize.MEDIUM -> MediumScreen(component)
        WindowSize.EXPANDED -> ExpandedScreen(component)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CompactScreen(
    component: HomeComponent
) {
    var selectedPage by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                component.pagerItems.forEach { item ->
                    NavigationBarItem(
                        selected = selectedPage == item.key,
                        icon = {
                            NavIcon(item)
                        },
                        onClick = {
                            component.selectPage(item.key)
                        },
                        label = {
                            Text(text = stringResource(item.label))
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Pages(
                pages = component.pages,
                onPageSelected = { index ->
                    component.selectPage(index)
                }
            ) { index, page ->
                selectedPage = index
                page.render()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MediumScreen(
    component: HomeComponent
) {
    var selectedPage by remember { mutableStateOf(0) }

    Scaffold {
        Row(modifier = Modifier.padding(it)) {
            NavigationRail(
                modifier = Modifier.fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.weight(1F))
                component.pagerItems.forEach { item ->
                    NavigationRailItem(
                        selected = selectedPage == item.key,
                        icon = {
                            NavIcon(item)
                        },
                        onClick = {
                            component.selectPage(item.key)
                        },
                        label = {
                            Text(text = stringResource(item.label))
                        },
                        alwaysShowLabel = false
                    )
                }
                Spacer(modifier = Modifier.weight(1F))
            }

            ExpandedPages(
                pages = component.pages
            ) { index, page ->
                selectedPage = index
                page.render()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpandedScreen(
    component: HomeComponent
) {
    var selectedPage by remember { mutableStateOf(0) }

    Scaffold {
        PermanentNavigationDrawer(
            modifier = Modifier.padding(it),
            drawerContent = {
                PermanentDrawerSheet(
                    modifier = Modifier.padding(start = 16.dp),
                    drawerShape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp,
                        bottomStart = 0.dp
                    )
                ) {
                    Spacer(modifier = Modifier.weight(1F))
                    component.pagerItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = {
                                NavIcon(item)
                            },
                            label = {
                                Text(text = stringResource(item.label))
                            },
                            onClick = {
                                component.selectPage(item.key)
                            },
                            selected = selectedPage == item.key
                        )
                    }
                    Spacer(modifier = Modifier.weight(1F))
                }
            }
        ) {
            Box(
                contentAlignment = Alignment.TopStart
            ) {
                ExpandedPages(
                    pages = component.pages
                ) { index, page ->
                    selectedPage = index
                    page.render()
                }
            }
        }
    }
}

@Composable
fun NavIcon(item: HomeComponent.PagerItem) {
    when (item.icon) {
        is ImageVector -> {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(item.label),
                modifier = Modifier.size(24.dp)
            )
        }
        is Painter -> {
            Icon(
                painter = item.icon,
                contentDescription = stringResource(item.label),
                modifier = Modifier.size(24.dp)
            )
        }
        is ImageResource -> {
            Icon(
                painter = painterResource(item.icon),
                contentDescription = stringResource(item.label),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}