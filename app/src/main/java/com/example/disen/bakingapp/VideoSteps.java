package com.example.disen.bakingapp;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.disen.bakingapp.databinding.FragmentVideoStepsBinding;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;


public class VideoSteps extends Fragment {
    SimpleExoPlayer player;
    RecipeSample sample;
    String previous_video;
    String next_video;
    int id;
    int size;
    int screenSize;
    String orientation;
    ArrayList<RecipeSample> arr_steps;

    String steps;
    String video;
    int position;
    String name;
    FragmentVideoStepsBinding fragmentVideoStepsBinding;


    public VideoSteps() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        setUserVisibleHint(true);
       /** int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            orientation = getString(R.string.landscape);
            editor.putString("orientation", orientation);
            editor.apply();
          //  getActivity().setTheme(R.style.FullscreenCustom);
        } else {
            orientation = getString(R.string.portrait);
            editor.putString("orientation", orientation);
            editor.apply();
           // getActivity().setTheme(R.style.ActionBarCustom);
        } */
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int currentOrientation = getResources().getConfiguration().orientation;

        if(isVisibleToUser){
            Log.e(MainActivity.class.getSimpleName(), "VISIBLEEE!!!!!!!!!!!!!!!! ");
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                orientation = getString(R.string.landscape);
                editor.putString("orientation", orientation);
                editor.apply();
                Log.e(VideoSteps.class.getSimpleName(), "landscape " + orientation);
            } else {
                orientation = getString(R.string.portrait);
                editor.putString("orientation", orientation);
                editor.apply();
                Log.e(VideoSteps.class.getSimpleName(), "landscape " + orientation);
            }
        }
        else{
            Log.e(MainActivity.class.getSimpleName(), "NOT VISIBLE!!! ");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentVideoStepsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_video_steps, container, false);
        //Initiate different variables
        screenSize = getResources().getConfiguration().smallestScreenWidthDp;

        if (screenSize >= 600) {

        }
        //if the user is using a tablet, get variables through passed bundle
        else {
            Bundle bundle = getArguments();
            steps = bundle.getString("step");
            video = bundle.getString("video");
            video = CheckIfVideoIsNull(video);
            position = bundle.getInt("position");
            size = bundle.getInt("size");
            name = bundle.getString("name");
            setUpUI(steps, video, position, name);
        }

        return fragmentVideoStepsBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("step", steps);
        outState.putString("video", video);
        outState.putInt("position", position);
        outState.putInt("id", id);
        outState.putInt("size", size);
        outState.putString("name", name);
    }

    //get video infos from the recipe name fragment interface and then set up the UI
    public void setUpvideo(String step, String video, int position, int size, String name) {
        this.steps = step;
        this.video = video;
        this.position = position;
        this.size = size;
        this.name = name;
        setUpUI(this.steps, this.video, this.position, this.name);

    }

    private String CheckIfVideoIsNull(String video){
        if(video.equals("")||video.equals(null)){
            video = "https://video_not_available.mp4";
        }
        return video;
    }

    //Once passed variables, set up the video step fragment UI
    public void setUpUI(String step, String video, int position, String name) {
        fragmentVideoStepsBinding.instruction.setText(step);
        id = position;

        if (name != null) {
            sample = RecipeSample.getSampleByName(getContext(), name);
        } else {
            sample = RecipeSample.getSampleByName(getContext(), getString(R.string.nutella));
        }
        arr_steps = sample.getSteps();
        updatePreviousandNextvideoFloatingButtons();
        fragmentVideoStepsBinding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                LoadpreviousVideo();
                id--;
                updatePreviousandNextvideoFloatingButtons();
            }

        });
        fragmentVideoStepsBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                LoadnextVideo();
                id++;
                updatePreviousandNextvideoFloatingButtons();
            }
        });
        if (video == null || video.equals("")) {
            video_not_available();
        } else {
            releasePlayer();
            initializePlayer(Uri.parse(video));
        }

    }

    //this is why video replaces container
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentManager fragmentManager = getFragmentManager();
            String steps = savedInstanceState.getString("step");
            String video = savedInstanceState.getString("video");
            int position = savedInstanceState.getInt("position");
            int size = savedInstanceState.getInt("size");
            int id = savedInstanceState.getInt("id");
            String name = savedInstanceState.getString("name");
            Bundle bundle = new Bundle();
            bundle.putString("step", steps);
            bundle.putString("name", name);
            bundle.putString("video", video);
            bundle.putInt("position", id);
            bundle.putInt("size", size);
            bundle.putInt("id", id);
            if (screenSize < 600) {
                VideoSteps videoSteps = new VideoSteps();
                videoSteps.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.container, videoSteps, "video_steps").addToBackStack(null).commit();
            }
        }
    }

    private void initializePlayer(Uri uri) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            fragmentVideoStepsBinding.video.setPlayer(player);
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(getContext(), "music"), new DefaultExtractorsFactory(),
                    null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            //player.seekTo(currentWindow, playbackPosition);
        }

    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    //Update previous and next floating buttons UI based on current video id
    private void updatePreviousandNextvideoFloatingButtons() {
        if (id <= 0) {
            fragmentVideoStepsBinding.previous.setVisibility(View.GONE);
        } else if (id >= size - 1) {
            fragmentVideoStepsBinding.next.setVisibility(View.GONE);
        } else {
            fragmentVideoStepsBinding.next.setVisibility(View.VISIBLE);
            fragmentVideoStepsBinding.previous.setVisibility(View.VISIBLE);
        }
    }

    //Play next video
    private void LoadnextVideo() {
        next_video = arr_steps.get(id + 1).getVideoUrl();
        video = next_video;
        steps = arr_steps.get(id + 1).getVideo_desc();
        if (next_video == null || next_video.equals("")) {
            video_not_available();
            fragmentVideoStepsBinding.instruction.setText(arr_steps.get(id + 1).getVideo_desc());
        } else {
            initializePlayer(Uri.parse(next_video));
            fragmentVideoStepsBinding.instruction.setText(arr_steps.get(id + 1).getVideo_desc());
        }
    }
    //Play previous video
    private void LoadpreviousVideo() {
        previous_video = arr_steps.get(id - 1).getVideoUrl();
        video = previous_video;
        steps = arr_steps.get(id - 1).getVideo_desc();
        if (previous_video == null || previous_video.equals("")) {
            video_not_available();
            fragmentVideoStepsBinding.instruction.setText(arr_steps.get(id - 1).getVideo_desc());
        } else {
            initializePlayer(Uri.parse(previous_video));
            fragmentVideoStepsBinding.instruction.setText(arr_steps.get(id - 1).getVideo_desc());
        }
    }

    public void video_not_available() {
        releasePlayer();
        initializePlayer(Uri.parse("https://video_not_available.mp4"));
        video = "https://video_not_available.mp4";
        if(orientation == "portrait"){
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.videolayout),getString(R.string.not_available), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
