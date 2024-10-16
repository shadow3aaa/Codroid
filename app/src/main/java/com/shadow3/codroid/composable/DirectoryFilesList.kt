package com.shadow3.codroid.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shadow3.codroid.R
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.isDirectory

@Composable
fun DirectoryFilesList(
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    path: String,
    onClick: (Path) -> Unit = {},
    onLongClick: (Path) -> Unit = {}
) {
    val state = remember {
        DirectoryFilesState()
    }
    val context = LocalContext.current
    LaunchedEffect(path) {
        state.setFileListPath(context = context, path = Path(path))
    }

    val fileList by state.filesList.collectAsState()
    val fileListPath by state.filesListPath.collectAsState()
    LazyColumn(modifier = modifier) {
        item {
            PathCard(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                contentColor = contentColor,
                containerColor = containerColor,
                path = fileListPath?.resolve("..") ?: Path(".."),
                metaInfo = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }

        items(items = fileList) { path ->
            PathCard(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                contentColor = contentColor,
                containerColor = containerColor,
                path = path,
                metaInfo = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview(heightDp = 60, widthDp = 200)
fun PathCard(
    modifier: Modifier = Modifier,
    path: Path = Path(path = "/root"),
    metaInfo: String? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onClick: (Path) -> Unit = {},
    onLongClick: (Path) -> Unit = {}
) {
    Card(
        modifier = modifier.combinedClickable(
            onClick = { onClick(path) },
            onLongClick = { onLongClick(path) }), shape = RectangleShape, colors = CardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            if (path.isDirectory()) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_folder),
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.icon_file),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = path.fileName.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor
                )
                if (metaInfo != null) {
                    Text(
                        text = metaInfo,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}