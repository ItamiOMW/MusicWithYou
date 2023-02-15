package com.example.musicwithyou.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.musicwithyou.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreatePlaylistDialog(
    modifier: Modifier = Modifier,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {

    var playlistTitleText by remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = modifier,
            elevation = 5.dp,
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.creating_playlist_title),
                    style = MaterialTheme.typography.subtitle1
                )
                TextField(
                    value = playlistTitleText,
                    textStyle = MaterialTheme.typography.body1,
                    onValueChange = { text ->
                        playlistTitleText = text
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.enter_title_hint),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.secondary.copy(alpha = 0.5f)
                        )
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(5.dp),
                        shape = CircleShape
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            style = MaterialTheme.typography.body2,
                        )
                    }
                    Button(
                        onClick = { onConfirm(playlistTitleText) },
                        modifier = Modifier.padding(5.dp),
                        shape = CircleShape
                    ) {
                        Text(
                            text = stringResource(R.string.create),
                            style = MaterialTheme.typography.body2,
                        )
                    }
                }
            }
        }
    }

}