package com.androidmobile.JR;

import java.util.ArrayList;

import java.util.List;
import com.androidmobile.menulateral.Section;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;



public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {
	   
    private ExpandableListView sectionListView;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
       
        List<Section> sectionList = createMenu();
               
        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
        this.sectionListView.setGroupIndicator(null);
       
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
        this.sectionListView.setAdapter(sectionListAdapter);
       
        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
              @Override
              public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
              }
            });
       
        this.sectionListView.setOnChildClickListener(this);
       
        int count = sectionListAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            this.sectionListView.expandGroup(position);
        }
       
        return view;
    }

    private List<Section> createMenu() {
        List<Section> sectionList = new ArrayList<Section>();

        Section oDemoSection = new Section("Demos");
        oDemoSection.addSectionItem(101,"List/Detail (Fragment)", "gazstation");
        oDemoSection.addSectionItem(102, "Airport (AsyncTask)", "gazstation");
       
        Section oGeneralSection = new Section("General");
        oGeneralSection.addSectionItem(201, "Settings", "gazstation");
        oGeneralSection.addSectionItem(202, "Rate this app", "gazstation");
        oGeneralSection.addSectionItem(203, "Eula", "gazstation");
        oGeneralSection.addSectionItem(204, "Quit", "gazstation");
       
        sectionList.add(oDemoSection);
        sectionList.add(oGeneralSection);
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {

        switch ((int)id) {
        case 101:
            //TODO
            break;
        case 102:
            //TODO
            break;
        case 201:
            //TODO
            break;
        case 202:
            //TODO
            break;
        case 203:
            //TODO
            break;
        case 204:
            //TODO
            break;
        }
       
        return false;
    }
}
