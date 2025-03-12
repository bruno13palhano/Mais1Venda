package com.bruno13palhano.data.notifications

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.annotation.RequiresPermission

object NewOrdersSchedule {
    @RequiresPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)
    fun scheduleJob(context: Context) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(
            OrderPollingService.JOB_ID,
            ComponentName(context, OrderPollingService::class.java),
        )
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
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
