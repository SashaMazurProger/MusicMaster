/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.acrcloud.utils;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import com.acrcloud.ui.select.SelectMusicViewModel;
import com.acrcloud.ui.select.SongAdapter;

/**
 * Created by amitshekhar on 11/07/17.
 */

public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }


    @SuppressWarnings("unchecked")
    @BindingAdapter({"items"})
    public static void list(RecyclerView listView, ObservableList<SelectMusicViewModel.Song> songList) {
        ((SongAdapter) listView.getAdapter()).setSongs(songList);
        listView.getAdapter().notifyDataSetChanged();
    }


//    @BindingAdapter({"adapter"})
//    public static void addBlogItems(RecyclerView recyclerView, List<BlogResponse.Blog> blogs) {
//        BlogAdapter adapter = (BlogAdapter) recyclerView.getAdapter();
//        if (adapter != null) {
//            adapter.clearItems();
//            adapter.addItems(blogs);
//        }
//    }
//
//    @BindingAdapter({"adapter"})
//    public static void addOpenSourceItems(RecyclerView recyclerView, List<OpenSourceItemViewModel> openSourceItems) {
//        OpenSourceAdapter adapter = (OpenSourceAdapter) recyclerView.getAdapter();
//        if (adapter != null) {
//            adapter.clearItems();
//            adapter.addItems(openSourceItems);
//        }
//    }
//
//    @BindingAdapter({"adapter", "action"})
//    public static void addQuestionItems(SwipePlaceHolderView mCardsContainerView, List<QuestionCardData> mQuestionList, int mAction) {
//        if (mAction == MainViewModel.ACTION_ADD_ALL) {
//            if (mQuestionList != null) {
//                mCardsContainerView.removeAllViews();
//                for (QuestionCardData question : mQuestionList) {
//                    if (question != null && question.options != null && question.options.size() == 3) {
//                        mCardsContainerView.addView(new QuestionCard(question));
//                    }
//                }
//                ViewAnimationUtils.scaleAnimateView(mCardsContainerView);
//            }
//        }
//    }
//
//    @BindingAdapter("imageUrl")
//    public static void setImageUrl(ImageView imageView, String url) {
//        Context context = imageView.getContext();
//        Glide.with(context).load(url).into(imageView);
//    }
}
