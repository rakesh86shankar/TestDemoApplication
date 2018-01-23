package com.example.ra283478.testdemoapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RA283478 on 1/11/2018.
 */

public class NewsPaperTypesFragment extends Fragment /*implements Observer*/ {

    View view;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    BaseActivity currrentBaseActivity;
    List<NewsPaperGenre> genres = new ArrayList<>();
    //NewsPaperTypesFragmentBinding newsPaperTypesFragmentBinding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*newsPaperTypesFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.layout_newspaper_types, container, false);
        view = newsPaperTypesFragmentBinding.getRoot();
        view.setMainViewModel(getActivity());*/

        view = inflater.inflate(R.layout.layout_newspaper_types, container, false);
        recyclerView = (RecyclerView)view. findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        layoutManager = new LinearLayoutManager(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadData();

    }

    public void setActivityCallBack(BaseActivity baseActivity){
        currrentBaseActivity = baseActivity;
    }

    public void downloadData(){
        try{
            APIInterface apiService =
                    APIClient.getClient().create(APIInterface.class);

            Call<NewsPaperList> call = apiService.getNewsPaperSources(APIClient.APIKey);
            call.enqueue(new Callback<NewsPaperList>() {
                @Override
                public void onResponse(Call<NewsPaperList> call, Response<NewsPaperList> response) {
                    Log.v("API Response",response.body().toString());
                    if(currrentBaseActivity!=null) {
                        HttpUrl url = call.request().url();
                        currrentBaseActivity.updateUI("sucess:>>>"+url.toString());
                        currrentBaseActivity.getStringResponse("sucess:>>>"+response.body().toString());
                        try{
                            NewsPaperList newsPaperList = response.body();
                            downloadData(newsPaperList);
                            currrentBaseActivity.updateUI("newsPaperList Size>>>>"+newsPaperList.getSources().size());
                        }catch (Exception e){
                            currrentBaseActivity.updateUI("Exception in getting newsPaperList Size>>>>"+e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewsPaperList> call, Throwable t) {
                    HttpUrl url = call.request().url();
                    if(currrentBaseActivity!=null) {
                        currrentBaseActivity.updateUI("Failure:>>>"+url.toString());
                        currrentBaseActivity.getStringResponse("Failure:>>>"+t.toString());
                    }
                    Log.v("API Response Url",url.toString());
                    Log.v("API Response Error",t.toString());
                }
            });

            /*final Call<ArticleList> articleList = apiService.getArticleList("bbc-sport",APIClient.APIKey);
            articleList.enqueue(new Callback<ArticleList>() {
                @Override
                public void onResponse(Call<ArticleList> call, Response<ArticleList> response) {
                    if(currrentBaseActivity!=null) {
                        HttpUrl url = call.request().url();
                        currrentBaseActivity.updateUI("sucess:>>>"+url.toString());
                        currrentBaseActivity.getStringResponse("sucess:>>>"+response.body().toString());
                        try{
                            ArticleList articleList1 = response.body();
                            currrentBaseActivity.updateUI("ArticleList Size>>>>"+articleList1.getArticles().size());
                        }catch (Exception e){
                            currrentBaseActivity.updateUI("Exception in getting ArticleList Size>>>>"+e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArticleList> call, Throwable t) {
                    if(currrentBaseActivity!=null) {
                        HttpUrl url = call.request().url();
                        currrentBaseActivity.updateUI("Failure:>>>"+url.toString());
                        currrentBaseActivity.getStringResponse("Failure:>>>"+t.toString());
                    }
                }
            });
*/


        }catch (Exception e){
            Log.v("Exception in downloading data",e.getMessage());
        }
    }

    public void downloadData(NewsPaperList newsPaperList){
        try{
            HashMap<String,NewsPaperGenre> newsPaperCollection = new HashMap<>();
            List<NewsPaperObject> newsPaperList1 =  newsPaperList.getSources();

            for(int i = 0 ; i <newsPaperList1.size() ; i++){
                NewsPaperObject newsPaperObject = newsPaperList1.get(i);
                NewsPaperGenre value = newsPaperCollection.get(newsPaperObject.getCategory());
                if (value != null) {
                    value.getPaperObjects().add(newsPaperObject);
                } else {
                    List<NewsPaperObject> newsPaperType = new ArrayList<>() ;
                    newsPaperType.add(newsPaperObject);
                    NewsPaperGenre newGenre = new NewsPaperGenre(newsPaperObject.getCategory(),newsPaperType);
                    newsPaperCollection.put(newsPaperObject.getCategory(),newGenre);
                }
            }
            for ( Map.Entry<String, NewsPaperGenre> entry : newsPaperCollection.entrySet()) {
                String key = entry.getKey();
                NewsPaperGenre genre = entry.getValue();
                genres.add(genre);
            }
            updateView();
        }catch (Exception e){
            currrentBaseActivity.updateUI("Excception in parsing Data"+e.getMessage());
        }

    }

    public void updateView(){
        //instantiate your adapter with the list of genres
        try{
            GenreAdapter adapter = new GenreAdapter(genres,getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            currrentBaseActivity.updateUI("Excception in updateView"+e.getMessage());
        }


    }

   /* @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof NewsPaperTypeModel){
            NewsPaperTypeModel newsPaperTypeModel = (NewsPaperTypeModel) observable;
            downloadData(newsPaperTypeModel.getNewsPaperList());
        }
    }*/
}
