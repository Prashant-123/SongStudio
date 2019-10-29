package com.songstudio.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.songstudio.R;
import com.songstudio.databinding.SongModelBinding;
import com.songstudio.fragments.PlayerFragment;
import com.songstudio.models.SongModel;

import java.util.ArrayList;


public class PaginatonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private Context context;

    private ArrayList<SongModel> list;

    public PaginatonListAdapter(Context context, ArrayList<SongModel> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SongModel model = list.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                ListVH listVH = (ListVH) holder;
                listVH.bind(model);

                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.song_model, parent, false);
        viewHolder = new ListVH((SongModelBinding) binding);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    public class ListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final SongModelBinding binding;

        public ListVH(SongModelBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SongModel songModel) {
            binding.setSong(songModel);
            binding.getRoot().setOnClickListener(this);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putSerializable("song", list.get(position));
            Fragment playerFragment = new PlayerFragment();
            playerFragment.setArguments(bundle);
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            ft.replace(R.id.main_frame, playerFragment);
            ft.commit();
            ft.addToBackStack(null);

        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
