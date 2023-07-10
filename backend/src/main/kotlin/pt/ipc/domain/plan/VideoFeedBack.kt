package pt.ipc.domain.plan

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class VideoFeedBack(
    @ColumnName("feedback_client") val clientFeedBack: String?,
    @ColumnName("feedback_monitor") val monitorFeedBack: String?
)
