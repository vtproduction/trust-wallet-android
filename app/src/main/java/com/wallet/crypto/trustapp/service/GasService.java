package com.wallet.crypto.trustapp.service;


import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class GasService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d("GASERVICE", "OnStartJob");

        return true; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }
}
