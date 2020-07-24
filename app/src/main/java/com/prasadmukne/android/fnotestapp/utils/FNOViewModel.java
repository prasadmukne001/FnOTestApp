package com.prasadmukne.android.fnotestapp.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prasad Mukne on 24-07-2020.
 */
public class FNOViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> fragmentStack = new MutableLiveData<ArrayList<String>>();

    public FNOViewModel(@NonNull Application application) {
        super(application);
        ArrayList arrayList = null;
        arrayList = loadDataFromSharedIfAvailable(arrayList);
        if (null == arrayList) {
            arrayList = new ArrayList();
        }
        fragmentStack.setValue(arrayList);
    }

    private ArrayList loadDataFromSharedIfAvailable(ArrayList arrayList) {
        String json = SharedPrefUtility.getInstance(getApplication()).getValue(SharedPrefUtility.FRAG_STACK);
        if (null != json && !json.equals("[]")) {
            Type type = new TypeToken<List<String>>() {
            }.getType();
            arrayList = new Gson().fromJson(json, type);
        }
        return arrayList;
    }

    public void addFragment(String name) {
        ArrayList arrayList = fragmentStack.getValue();
        if (null == arrayList)
            arrayList = new ArrayList();
        String nameToAdd = name.toLowerCase();
        arrayList.add(nameToAdd);
        fragmentStack.setValue(arrayList);
        saveDataToSharedPref(arrayList);
    }

    public void removeFragment(String position) {
        ArrayList arrayList = fragmentStack.getValue();
        arrayList.remove(position);
        saveDataToSharedPref(arrayList);
        fragmentStack.setValue(arrayList);
    }

    private void saveDataToSharedPref(ArrayList arrayList) {
        ArrayList list = new ArrayList(arrayList);
        SharedPrefUtility.getInstance(getApplication()).putValue(SharedPrefUtility.FRAG_STACK, new Gson().toJson(list));
    }

    public ArrayList<String> getFragmentStack() {
        return fragmentStack.getValue();
    }

    public boolean checkIfFragNameExists(String name) {
        String nameToSearch = name.toLowerCase();
        return fragmentStack.getValue().contains(nameToSearch);
    }

    public int getFragByPosition(String name) {
        String nameToSearch = name.toLowerCase();
        return fragmentStack.getValue().indexOf(nameToSearch);
    }

    public MutableLiveData getData() {
        return fragmentStack;
    }
}
