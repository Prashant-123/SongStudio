package com.songstudio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.songstudio.R;
import com.songstudio.models.SongModel;
import com.songstudio.Utils.CommonValues;
import com.songstudio.adapters.PaginationScrollListener;
import com.songstudio.adapters.PaginatonListAdapter;
import com.songstudio.databinding.HomeFragmentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends MyFragment {

    private HomeFragmentBinding binding;
    private PaginatonListAdapter adapter;
    public static ArrayList<SongModel> songs = new ArrayList<>();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = CommonValues.TOTAL_PAGES;
    private int currentPage = PAGE_START;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.home_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new PaginatonListAdapter(getContext(), songs);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        LoadFirstPage();

        binding.recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                LoadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    public void LoadFirstPage() {

        showProgress("Loading Songs...", true);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, CommonValues.BASE_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                dismissProgress();

                try {
                    if (response.length() > 0) {
                        for(int i=0; i<response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String song = jsonObject.getString("song");
                            String url = jsonObject.getString("url");
                            String artists = jsonObject.getString("artists");
                            String cover_image = jsonObject.getString("cover_image");

                            SongModel songModel = new SongModel();
                            songModel.setSong_name(song);
                            songModel.setUrl(url);
                            songModel.setArtist(artists);
                            songModel.setCover_image(cover_image);

                            songs.add(songModel);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "Your playlist is empty...", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
            }
        });

        queue.add(jsonArrayRequest);

        if (currentPage <= CommonValues.TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    public void LoadNextPage() {

        adapter.removeLoadingFooter();
        isLoading = false;

//        String URL = "http://starlord.hackerearth.com/studio/page-" + currentPage; //If API is Paginated

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, CommonValues.BASE_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    if (response.length() > 0) {
                        for(int i=0; i<response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String song = jsonObject.getString("song");
                            String url = jsonObject.getString("url");
                            String artists = jsonObject.getString("artists");
                            String cover_image = jsonObject.getString("cover_image");

                            SongModel songModel = new SongModel();
                            songModel.setSong_name(song);
                            songModel.setUrl(url);
                            songModel.setArtist(artists);
                            songModel.setCover_image(cover_image);

                            songs.add(songModel);
                            adapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);

        if (currentPage != CommonValues.TOTAL_PAGES) adapter.addLoadingFooter();
        else {
            isLastPage = true;
            Snackbar.make(binding.getRoot(), "You've reached the Last page.", Snackbar.LENGTH_LONG).show();
        }

    }
}
