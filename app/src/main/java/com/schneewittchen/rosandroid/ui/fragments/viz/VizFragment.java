package com.schneewittchen.rosandroid.ui.fragments.viz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.viewmodel.VizViewModel;
import com.schneewittchen.rosandroid.ui.general.DataListener;
import com.schneewittchen.rosandroid.ui.general.WidgetChangeListener;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;


/**
 * TODO: Description
 *
 * @author Nico Studt
 * @version 1.0.2
 * @created on 10.01.20
 * @updated on 21.04.20
 * @modified by Nils Rottmann
 * @modified by jbravo@uma.es Juan Bravo-Arrabal
 */
public class VizFragment extends Fragment implements DataListener, WidgetChangeListener {

    public static final String TAG = VizFragment.class.getSimpleName();

    private VizViewModel mViewModel;
    private WidgetViewGroup widgetViewGroupview;
    private DrawerLayout drawerLayout;
    private ImageButton optionsOpenButton;
    private SwitchMaterial vizEditModeSwitch;
    private WebView mywebView;
    private WebView mywebView2;

    String url;
    String url2 = "192.168.1.224:8081,190"; //  "IP:port, zoom" --> For DT visualization (screen stream using Deskreen Viewer: www.deskreen.com).
    String scale; //scale is percent of zoom level you want
    String scale2;

    public static VizFragment newInstance() {
        return new VizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button_us = (Button) view.findViewById(R.id.uscam);
        Button button_ipcam = (Button) view.findViewById(R.id.ipcam);

        mywebView = (WebView) view.findViewById(R.id.camS);

        widgetViewGroupview = view.findViewById(R.id.widget_groupview);
        widgetViewGroupview.setDataListener(this);
        widgetViewGroupview.setOnWidgetDetailsChanged(this);

        optionsOpenButton = view.findViewById(R.id.viz_options_open_button);
        drawerLayout = view.findViewById(R.id.viz_options_drawer);
        drawerLayout.setScrimColor(getResources().getColor(R.color.drawerFadeColor));
        vizEditModeSwitch = view.findViewById(R.id.edit_viz_switch);

        /////////////////////////////////////////////////////

        WebSettings webSettings= mywebView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        mywebView.setWebViewClient(new WebViewClient());

        button_ipcam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AutoCompleteTextView input = (AutoCompleteTextView) view.findViewById(R.id.url);
                url = "192.168.1.200:3131/";
                url = url + input.getText().toString(); //url,port
                scale = String.valueOf(180); //zoom
                mywebView.setInitialScale(Integer.parseInt(scale));
                mywebView.loadUrl(url);
            }
        });

        button_us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String[] separated = url2.split(",");
                url2 = separated[0];
                scale2 = separated[1];
                mywebView.setInitialScale(Integer.parseInt(scale2));
                mywebView.loadUrl(url2);
            }
        });

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(VizViewModel.class);

        mViewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), widgetEntities -> {
            widgetViewGroupview.setWidgets(widgetEntities);
        });

        mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            widgetViewGroupview.onNewData(data);
        });

        vizEditModeSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            widgetViewGroupview.setVizEditMode(isChecked);
        });

        optionsOpenButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    @Override
    public void onNewWidgetData(BaseData data) {
        mViewModel.publishData(data);

    }

    @Override
    public void onWidgetDetailsChanged(BaseEntity widgetEntity) {
        mViewModel.updateWidget(widgetEntity);


    }
}
