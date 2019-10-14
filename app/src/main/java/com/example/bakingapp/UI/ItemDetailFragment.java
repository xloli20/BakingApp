package com.example.bakingapp.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bakingapp.Models.RecipesSteps;
import com.example.bakingapp.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link StepsActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    private static final String TAG = ItemDetailFragment.class.getSimpleName();

    private Context context;
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;

    private RecipesSteps currentStep;
    private ArrayList<RecipesSteps> steps;

    private TextView instructionTextView;
    private CollapsingToolbarLayout appBarLayout;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        context = container.getContext();

        //views
        instructionTextView = rootView.findViewById(R.id.instruction);
        playerView = rootView.findViewById(R.id.playerView);
        Activity activity = this.getActivity();
        assert activity != null;
        appBarLayout = activity.findViewById(R.id.toolbar_layout);

        if(savedInstanceState != null){
            currentStep = savedInstanceState.getParcelable("step");
            steps = savedInstanceState.getParcelableArrayList("steps");
            instructionTextView.setText(currentStep.getsInstructions());
            appBarLayout.setTitle(currentStep.getsDescription());
            initializePlayer(Uri.parse(currentStep.getsVideoUrl()));
            simpleExoPlayer.seekTo(savedInstanceState.getLong("video_position"));

        }else {
            assert getArguments() != null;
            if (getArguments().containsKey("step")) {
                String step = getArguments().getString("step");
                String video = getArguments().getString("video");
                currentStep = getArguments().getParcelable("steps");
                instructionTextView.setText(step);
                initializePlayer(Uri.parse(video));

            } else {
                Intent intent = Objects.requireNonNull(getActivity()).getIntent();
                currentStep = intent.getParcelableExtra("steps");
                steps = intent.getParcelableArrayListExtra("step_list");
                instructionTextView.setText(currentStep.getsInstructions());
                initializePlayer(Uri.parse(currentStep.getsVideoUrl()));

                if (appBarLayout != null) {
                    appBarLayout.setTitle(currentStep.getsDescription());
                }
                FloatingActionButton fab = getActivity().findViewById(R.id.fab);
                fab.setOnClickListener(view -> {
                    if(Integer.valueOf(currentStep.getsId()) < steps.size() -1) {
                        int sid = Integer.valueOf(currentStep.getsId()) +1;
                        currentStep = steps.get(sid);
                        appBarLayout.setTitle(currentStep.getsDescription());
                        instructionTextView.setText(currentStep.getsInstructions());
                        releasePlayer();
                        initializePlayer(Uri.parse(currentStep.getsVideoUrl()));
                    }
                });
            }
        }

        Button nextButton = rootView.findViewById(R.id.next_step);
        nextButton.setOnClickListener(view -> {
            if(Integer.valueOf(currentStep.getsId()) < steps.size() -1) {
                int sid = Integer.valueOf(currentStep.getsId()) +1;
                currentStep = steps.get(sid);
                appBarLayout.setTitle(currentStep.getsDescription());
                instructionTextView.setText(currentStep.getsInstructions());
                releasePlayer();
                initializePlayer(Uri.parse(currentStep.getsVideoUrl()));
            }
        });

        Button prevButton = rootView.findViewById(R.id.prev_step);
        prevButton.setOnClickListener(view -> {
            if(Integer.valueOf(currentStep.getsId()) > 0){
                int sid = Integer.valueOf(currentStep.getsId()) -1;
                currentStep = steps.get(sid);
                appBarLayout.setTitle(currentStep.getsDescription());
                instructionTextView.setText(currentStep.getsInstructions());
                releasePlayer();
                initializePlayer(Uri.parse(currentStep.getsVideoUrl()));
            }
        });

        //check orientation and full screen the video if it's landscape
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            ViewGroup.LayoutParams params = playerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = height;
            playerView.setLayoutParams(params);

            AppBarLayout appBarLayout = activity.findViewById(R.id.app_bar);
            appBarLayout.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("step",currentStep);
        outState.putParcelableArrayList("steps",steps);
        outState.putLong("video_position",simpleExoPlayer.getCurrentPosition());
        Log.d(TAG, "onSaveInstanceState: position " +simpleExoPlayer.getContentPosition());

    }

    private void initializePlayer(Uri mediaUri) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            // Bind the player to the view.
            playerView.setPlayer(simpleExoPlayer);
            // Produces DataSource instances through which media data is loaded.
            String userAgent = Util.getUserAgent(context, "BakingApp");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            // This is the MediaSource representing the media to be played.
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaUri);

            // Prepare the player with the source.
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        simpleExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
