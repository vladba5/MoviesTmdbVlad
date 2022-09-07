package com.example.moviestmdb.ui_people

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moviestmdb.PopularActor

@Composable
fun ActorCard(
    modifier: Modifier = Modifier,
    actorData: PopularActor,
    selectCard: (Int) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .clickable(
                onClick = { selectCard(actorData.id) }
            ),
        color = MaterialTheme.colors.onBackground,
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier
            .width(120.dp)
            .height(250.dp)) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = actorData.name,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 12.dp),
                text = actorData.knownForTitles,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
            )
        }


    }
}

//@Composable
//@Preview()
//private fun HomePosterPreviewDark() {
//    ActorCard(
//            poster = Poster.mock()
//        )
//}
