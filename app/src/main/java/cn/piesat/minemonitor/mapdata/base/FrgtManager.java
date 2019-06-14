package cn.piesat.minemonitor.mapdata.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

public class FrgtManager {

	public static void addStackChangedListener(FragmentActivity activity, OnBackStackChangedListener listener) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.addOnBackStackChangedListener(listener);
	}

	public static void removeStackChangedListener(FragmentActivity activity, OnBackStackChangedListener listener) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.removeOnBackStackChangedListener(listener);
	}

	public static void addFragment(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.add(layoutId, fragment, tag);
		fragmentTransaction.commit(); // 提交事务
	}

	public static void addAndShowFragment(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.add(layoutId, fragment, tag);
		fragmentTransaction.show(fragment);
		fragmentTransaction.commit(); // 提交事务
	}

	public static Fragment getCurrFragment(FragmentActivity activity, int layoutId) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(layoutId);
		return fragment;
	}

	/**
	 * @description 替换容器中的fragment
	 * @param fragment
	 *            要替换成的fragment
	 * */
	public static void replaceFragment(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.replace(layoutId, fragment, tag);
		fragmentTransaction.addToBackStack(tag);
		fragmentTransaction.commitAllowingStateLoss();
	}
	/**
	 * @description 替换容器中的fragment
	 * @param fragment
	 *            要替换成的fragment
	 * */
	public static void replaceFragment1(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = fragment.getChildFragmentManager();
//		fragment.getChildFragmentManager()
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.replace(layoutId, fragment, tag);
		fragmentTransaction.addToBackStack(tag);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public static void replaceFragmentToLayoutId(FragmentActivity activity, FragmentTransaction fragmentTransaction, int fragmengid, Fragment fragment, String tag) {
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.replace(fragmengid, fragment, tag);
		fragmentTransaction.addToBackStack(tag);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	public static void replaceFragmentNotStack(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.replace(layoutId, fragment, tag);
		// fragmentTransaction.addToBackStack(null);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static Fragment findFragmentByTag(FragmentActivity activity, String tag) {
		FragmentManager frgtManager = activity.getSupportFragmentManager();
		return frgtManager.findFragmentByTag(tag);
	}

	public static void hideFragment(FragmentActivity activity, Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.hide(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static void showFragment(FragmentActivity activity, Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.show(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static void removeFragment(FragmentActivity activity, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.remove(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
//		8.void popBackStack(String name, int flags);　
//		针对第一个参数，如果name为null，那么只有顶部的状态被弹出；如果name不为null，并且找到了这个name所指向的Fragment对象；
//		根据flags的值，如果是flag=0，那么将会弹出该状态以上的所有状态；如果flag=POP_BACK_STACK_INCLUSIVE，那么将会弹出该状态（包括该状态）以上的所有状态。
		fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	public static void removeOneFragment(FragmentActivity activity, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.remove(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	public static boolean popAllFragment(Fragment fragment, FragmentActivity activity){
		try {
			FragmentManager fragmentManager = activity.getSupportFragmentManager();
			return fragmentManager.popBackStackImmediate(fragment.getClass().getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isHaveFragments(FragmentActivity activity) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		int count = fragmentManager.getBackStackEntryCount();
		return count > 0 ? false : true;
	}

	public static List<Fragment> getFragments(FragmentActivity activity) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		List<Fragment> fragments = fragmentManager.getFragments();
		return fragments;
	}
	
	public static void switchFragment(FragmentActivity fActivity, int resId, Fragment from, Fragment to, String tag) {
		if (to.isAdded()) {
			getFTransaction(fActivity).hide(from).show(to).commit();
			return;
		}
//		 getFTransaction(fActivity).add(to, tag).hide(from).show(to).commit();
		getFTransaction(fActivity).add(resId, to, tag).hide(from).show(to).commit();
	}
	
	private static FragmentTransaction getFTransaction(FragmentActivity fActivity) {
		return fActivity.getSupportFragmentManager().beginTransaction();
	}
}
