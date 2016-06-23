package com.taobao.uikit.feature.features;

import com.taobao.uikit.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Created by kangyong.lt on 14-4-17.
 */
public class FeatureFactory {

	private static final HashMap<String, AttachInfo> featureMap = new HashMap<String, AttachInfo>();
	private static FeatureFactory mSelf = new FeatureFactory();

	static final int PRIORITY_HIGHEST = 1000;
	static final int PRIORITY_ABOVE_NORMAL = 750;
	static final int PRIORITY_NORMAL = 500;
	static final int PRIORITY_BELOW_NORMAL = 250;
	static final int PRIORITY_LOWEST = 0;

	private static class AttachInfo {
		int id;
		int priority;

		public AttachInfo(int id, int priority) {
			this.id = id;
			this.priority = priority;
		}

	}

	static {
		featureMap.put("ClickDrawableMaskFeature", new AttachInfo(
				R.styleable.FeatureNameSpace_uik_clickDrawableMaskFeature,
				PRIORITY_ABOVE_NORMAL));
		featureMap
				.put("RatioFeature", new AttachInfo(
						R.styleable.FeatureNameSpace_uik_ratioFeature,
						PRIORITY_NORMAL));
		featureMap.put("RoundRectFeature", new AttachInfo(
				R.styleable.FeatureNameSpace_uik_roundRectFeature,
				PRIORITY_NORMAL));
		featureMap
				.put("RoundFeature", new AttachInfo(
						R.styleable.FeatureNameSpace_uik_roundFeature,
						PRIORITY_NORMAL));
		featureMap.put("ClickViewMaskFeature", new AttachInfo(
				R.styleable.FeatureNameSpace_uik_clickViewMaskFeature,
				PRIORITY_BELOW_NORMAL));
		featureMap.put("BinaryPageFeature", new AttachInfo(
				R.styleable.FeatureNameSpace_uik_binaryPageFeature,
				PRIORITY_NORMAL));
		featureMap.put("PinnedHeaderFeature", new AttachInfo(
				R.styleable.FeatureNameSpace_uik_pinnedHeaderFeature,
				PRIORITY_NORMAL));
		featureMap.put("PullToRefreshFeature", new AttachInfo(
				R.styleable.FeatureNameSpace_uik_pullToRefreshFeature,
				PRIORITY_NORMAL));
		featureMap.put("StickyScrollFeature", new AttachInfo(
				R.styleable.FeatureNameSpace_uik_stickyScrollFeature,
				PRIORITY_NORMAL));
        featureMap.put("ParallaxScrollFeature", new AttachInfo(
                R.styleable.FeatureNameSpace_uik_parallaxScrollFeature,
                PRIORITY_NORMAL));
        featureMap.put("BounceScrollFeature", new AttachInfo(
                R.styleable.FeatureNameSpace_uik_bounceScrollFeature,
                PRIORITY_NORMAL));
        
        featureMap.put("PencilShapeFeature", new AttachInfo(
                R.styleable.FeatureNameSpace_uik_pencilShapeFeature,
                PRIORITY_NORMAL));
        featureMap.put("AutoScaleFeature", new AttachInfo(
                R.styleable.FeatureNameSpace_uik_autoScaleFeature,
                PRIORITY_NORMAL));
        featureMap.put("RotateFeature", new AttachInfo(
        		R.styleable.FeatureNameSpace_uik_rotateFeature, PRIORITY_NORMAL));
        featureMap.put("ImageSaveFeature", new AttachInfo(
        		R.styleable.FeatureNameSpace_uik_imagesavefeature, PRIORITY_NORMAL));
        featureMap.put("CellAnimatorFeature", new AttachInfo(
                R.styleable.FeatureNameSpace_uik_cellAnimatorFeature, PRIORITY_NORMAL));
        featureMap.put("RecyclerCellAnimatorFeature", new AttachInfo(R.styleable.FeatureNameSpace_uik_recyclerCellAnimatorFeature, PRIORITY_NORMAL));
        featureMap.put("DragToRefreshFeature", new AttachInfo(R.styleable.FeatureNameSpace_uik_dragToRefreshFeature, PRIORITY_NORMAL));
    }
	
	public static <T extends View> ArrayList<AbsFeature<? super T>> creator(
			Context context, TypedArray typedArray) {
		ArrayList<AbsFeature<? super T>> features = new ArrayList<AbsFeature<? super T>>();

        for (Entry<String, AttachInfo> entry : featureMap.entrySet())
        {

            String feature = entry.getKey();
            int id = entry.getValue().id;

            if (id >= 0)
            {
                boolean flag = typedArray.getBoolean(id, false);
                if (flag)
                {
                    try
                    {
                        @SuppressWarnings("unchecked") Class<AbsFeature<? super T>> clazz = (Class<AbsFeature<? super T>>) Class.forName(mSelf.getClass().getPackage().getName() + "." + feature);
                        features.add(clazz.newInstance());
                    }
                    catch (ClassNotFoundException e)
                    {
                        Log.e("Android UiKit", "can't find feature by id");
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return features;
	}

	public static int getFeaturePriority(String name) {
		if (featureMap.containsKey(name)) {
			AttachInfo info = featureMap.get(name);
			return info.priority;
		}
		return 0;
	}
}
