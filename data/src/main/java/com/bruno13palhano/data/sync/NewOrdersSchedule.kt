package com.bruno13palhano.data.sync

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

object NewOrdersSchedule {
    fun scheduleJob(context: Context) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(
            OrderPollingService.JOB_ID,
            ComponentName(context, OrderPollingService::class.java),
        )
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
//            .setPeriodic(15 * 60 * 1000) // 15 minutes
            .setOverrideDeadline(1 * 60 * 1000)
            .build()
        val result = jobScheduler.schedule(jobInfo)
        if (result == JobScheduler.RESULT_SUCCESS) {
            println("Job scheduled successfully")
        } else {
            println("Job scheduling failed")
        }
    }
}
