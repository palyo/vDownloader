package com.video.downloader.model

data class Graphql(
    val shortcode_media: ShortcodeMedia
)

data class ShortcodeMedia(
    val display_url: String,
    val is_video: Boolean,
    val video_url: String?,
    val edge_sidecar_to_children: EdgeSidecarToChildren?
)

data class EdgeSidecarToChildren(
    val edges: List<Edge>
)

data class Edge(
    val node: Node
)

data class Node(
    val is_video: Boolean,
    val video_url: String?,
    val display_url: String
)
