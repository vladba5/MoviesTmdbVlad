package com.example.moviestmdb.ui_people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.compose.rememberAsyncImagePainter
import com.example.moviestmdb.PopularActor
import com.example.moviestmdb.core.TmdbImageManager
import com.example.moviestmdb.core.extensions.launchAndRepeatWithViewLifecycle
import com.example.moviestmdb.core_ui.util.SpaceItemDecoration
import com.example.moviestmdb.core_ui.util.showToast
import com.example.moviestmdb.ui_people.databinding.PeopleFragmentBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class LobbyPeopleFragment @Inject constructor() : Fragment() {

    //    lateinit var binding: PeopleFragmentBinding
    private val viewModel: PeopleLobbyViewModel by viewModels()

    private lateinit var popularActorsAdapter: PopularActorsAdapter

    @Inject
    lateinit var tmdbImageManager: TmdbImageManager

    @Inject
    lateinit var tmdbImageUrlProvider: TmdbImageUrlProvider

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View = ComposeView(requireContext())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchAndRepeatWithViewLifecycle {
            (view as ComposeView).setContent {

                PeopleGrid(
                    viewModel.pagedList.collectAsLazyPagingItems(),
                    tmdbImageUrlProvider,
                )
            }
        }
    }

    @Composable
    fun ActorItem(
        modifier: Modifier = Modifier,
        actors: List<PopularActor>,
        selectPoster: (Long) -> Unit = {},
    ) {
        val listState = rememberLazyListState()
        Column(
            modifier = modifier
                .systemBarsPadding()
                .background(MaterialTheme.colors.background)
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(4.dp)
            ) {
                    items(20)
                     {
//                     Card()
                    }

            }
        }
    }


    @Composable
    fun PeopleGrid(
        lazyPeopleItems: LazyPagingItems<PopularActor>,
        tmdbImageUrlProvider: TmdbImageUrlProvider
    ) {

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(2.dp)
        ) {
            items(itemContent = { index ->
                lazyPeopleItems[index]?.let { actor ->
                    PersonItem(
                        person = actor,
                        tmdbImageUrlProvider = tmdbImageUrlProvider,
                    )
                }
            }, count = lazyPeopleItems.itemCount)
        }
    }

    @Composable
    fun PersonItem(
        modifier: Modifier = Modifier,
        person: PopularActor,
        tmdbImageUrlProvider: TmdbImageUrlProvider,
    ) {
        Card(
            elevation = 8.dp,
            modifier = modifier
                .width(120.dp)
                .height(200.dp)
                .padding(4.dp)
        ) {
            val url = tmdbImageUrlProvider.getBackdropUrl(person.profile_path, 200)
//            Image(
//                painter = rememberAsyncImagePainter(url),
//                contentDescription = null,
//                modifier = Modifier.size(128.dp)
//            )
            personImage(tmdbImageUrlProvider = tmdbImageUrlProvider, url = person.profile_path, width = 120)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .safeContentPadding()
                        .background(
                            color = Color.Blue,
                        )
                        .height(IntrinsicSize.Min)
                ) {
                    PersonName(
                        person.name,
                        color = Color.Green,
                    )
                    PersonKnownForTitles(
                        titles = person.knownForTitles,
                        color = Color.Red,
                    )
                }
            }
        }
    }


    @Composable
    fun PersonName(
        title: String,
        color: Color
    ) {
        Text(
            color = color,
            text = title,
            maxLines = 1,
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis
        )
    }


    @Composable
    fun PersonKnownForTitles(
        titles: String,
        color: Color
    ) {
        Text(
            color = color,
            text = titles,
            maxLines = 1,
            style = MaterialTheme.typography.body2,
            overflow = TextOverflow.Ellipsis
        )
    }


    @Composable
    fun personImage(
        tmdbImageUrlProvider: TmdbImageUrlProvider,
        url: String,
        width: Int
    ) {
        loadPicture(
            url = tmdbImageUrlProvider.getBackdropUrl(url, width),
            placeholder = painterResource(id = com.example.moviestmdb.ui_people.R.drawable.wallpaper)
        )?.let { pointer ->
            Image(
                painter = pointer,
                contentDescription = "person image",
                modifier = Modifier.fillMaxWidth().clip(CircleShape)
                )
        }
    }




    //    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View = ComposeView(inflater.context).apply {
//        layoutParams = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT)
//
//        launchAndRepeatWithViewLifecycle {
//            (view as ComposeView).setContent {
//                PeopleGrid(
//                    viewModel.pagedList.collectAsLazyPagingItems(),
//                    tmdbImageUrlProvider,
//                )
//            }
//        }
//    }
}