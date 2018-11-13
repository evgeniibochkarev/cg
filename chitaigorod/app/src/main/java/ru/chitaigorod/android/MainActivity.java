package ru.chitaigorod.android;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import com.roughike.bottombar.*;
import java.util.*;
import ru.chitaigorod.android.*;
import ru.chitaigorod.android.UX.fragments.*;

import ru.chitaigorod.android.R;

import ru.chitaigorod.android.UX.custom_view.*;
import ru.chitaigorod.android.utils.*;
import android.webkit.*;
import ru.chitaigorod.android.UX.dialogs.*;
import java.util.concurrent.*;


public class MainActivity extends AppCompatActivity implements FragNavController.TransactionListener, FragNavController.RootFragmentListener ,  OnTabReselectListener, OnTabSelectListener, BaseFragment.FragmentNavigation, BaseDialogFragment.FragmentNavigation// FragNavController.TransactionListener, FragNavController.RootFragmentListener ,ontab//BaseFragment.BaseFragmentCallbacks
{

	@Override
	public void setCartBadge(int count)
	{
		BottomBarTab nearby = mBottomBar.getTabWithId(R.id.tab_cart);
		nearby.setBadgeCount(count);
		
	}
	
	@Override
	public void onTabSelected(@IdRes int menuItemId) {
		switch (menuItemId) {
			case R.id.tab_main_page:
				mNavController.switchTab(INDEX_MAIN_PAGE);
				break;
			case R.id.tab_cart:
				mNavController.switchTab(INDEX_CART);
				break;
			case R.id.tab_search:
				mNavController.switchTab(INDEX_SEARCH);
				break;
			case R.id.tab_cabinet:
				mNavController.switchTab(INDEX_CABINET);
				break;
			case R.id.tab_info:
				mNavController.switchTab(INDEX_INFO);
				break;
		}
	}

	@Override
	public void onTabReSelected(@IdRes int menuItemId) {
	
		mNavController.clearStack();

		switch (menuItemId) {
			case R.id.tab_main_page:
				mNavController.switchTab(INDEX_MAIN_PAGE);
				break;
			case R.id.tab_cart:
				mNavController.switchTab(INDEX_CART);
				break;
			case R.id.tab_search:
				mNavController.switchTab(INDEX_SEARCH);
				break;
			case R.id.tab_cabinet:
				mNavController.switchTab(INDEX_CABINET);
				break;
			case R.id.tab_info:
				mNavController.switchTab(INDEX_INFO);
				break;
		}
		
	}
	
	
	




	
	private BottomBar mBottomBar;

    private FragNavController mNavController ;
	private FragmentHistory fragmentHistory;

    private WebView wv;
    //Better convention to properly name the indices what they are in your app
    private final int INDEX_MAIN_PAGE = FragNavController.TAB1;
    private final int INDEX_CART = FragNavController.TAB2;
    private final int INDEX_SEARCH = FragNavController.TAB3;
    private final int INDEX_CABINET = FragNavController.TAB4;
    private final int INDEX_INFO = FragNavController.TAB5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        	
        fragmentHistory = new FragmentHistory();


        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
			.transactionListener(this)
			.rootFragmentListener(this, 5)
			.build();


        mNavController. switchTab(0);

     
		
		
		mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
	
		mBottomBar.setOnTabSelectListener(this);
		mBottomBar.setOnTabReselectListener(this);
        
		wv = new WebView(getApplicationContext());

		wv.setWebViewClient(new MyWebViewClient());
		wv.setWebChromeClient(new MyWebChromeClient(this));
		// включаем поддержку JavaScript
		wv.getSettings().setDomStorageEnabled(true);
		wv.getSettings().setJavaScriptEnabled(true);
		// указываем страницу загрузки
		wv.loadUrl("https://www.chitai-gorod.ru/"); 
		
		}

	public void get(String req) {
		if(wv != null){
			wv.loadUrl(req);
		}
	}
	
	public FragNavController getFragmentController(){
		return mNavController;
	}
	
	
	@Override
    public void onBackPressed() {

        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {

            if (fragmentHistory.isEmpty()) {
                super.onBackPressed();
            } else {


                if (fragmentHistory.getStackSize() > 1) {

                    int position = fragmentHistory.popPrevious();

                    mNavController. switchTab(position);

                    updateTabSelection(position);

                } else {

					mNavController.
                    	switchTab(0);

                    updateTabSelection(0);

                    fragmentHistory.emptyStack();
                }
            }

        }
    }


    private void updateTabSelection(int currentTab){

		/*
        for (int i = 0; i <  TABS.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
            if(currentTab != i) {
                selectedTab.getCustomView().setSelected(false);
            }else{
                selectedTab.getCustomView().setSelected(true);
            }
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       /* if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }*/
    }

	public void showDialog(DialogFragment frg)
	{
		if (mNavController != null) {
            mNavController.showDialogFragment(frg);//.pushFragment(fragment);
        }
	}
	
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }


    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {


            updateToolbar();

        }
    }

    private void updateToolbar() {
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setDisplayShowHomeEnabled(!mNavController.isRootFragment());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
		*/
    }


    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        //do fragmentty stuff. Maybe change title, I'm not going to tell you how to live your life
        // If we have a backstack, show the back button
        if (getSupportActionBar() != null && mNavController != null) {

            updateToolbar();

        }
    }

	
    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {

            case FragNavController.TAB1:
                return new MainPageFragment().newInstance();
            case FragNavController.TAB2:
                return new CartFragment().newInstance();
            case FragNavController.TAB3:
                return new SearchFragment().newInstance();
            case FragNavController.TAB4:
                return new CabinetFragment().newInstance();
            case FragNavController.TAB5:
                return new InfoFragment().newInstance();


        }
        throw new IllegalStateException("Need to send an index that we know");
    }


//    private void updateToolbarTitle(int position){
//
//
//        getSupportActionBar().setTitle(TABS[position]);
//
//    }


    


	

	/*
	@Override
	public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId)
	{
		
		switch (newTabId) {
          	case R.id.tab_main_page:
                selectedTab(Constants.TAB_MAIN_PAGE_FRAGMENT);
               return false;
			  //break;
			case R.id.tab_discount:
                selectedTab(Constants.TAB_DISCOUNT_FRAGMENT);
                return false;
			//  break;
			case R.id.tab_catalog:
                selectedTab(Constants.TAB_CATALOG_FRAGMENT);
                return false;
			  	//break;
			case R.id.tab_cabinet:
                selectedTab(Constants.TAB_CABINET_FRAGMENT);
				//break;
                return false;
			case R.id.tab_info:
                selectedTab(Constants.TAB_INFO_FRAGMENT);
                return false;
			    //break;
        }
       
		return true;
	}
	
	public static final String Tag = "MainPage";
	
	private Map<String, Stack<Fragment>> stacks;
    private String currentTab;
   
    private List<String> stackList;
    private List<String> menuStacks;
    private Fragment currentFragment;
	private Fragment mainPageFragment ;
	private Fragment discountFragment ;
	private Fragment catalogFragment ;
	private Fragment cabinetFragment ;
	private Fragment infoFragment ;
	

	//private static MainActivity mInstance = null;

	// указывает может ли приложение закрыть след. backpress'ом
    private boolean isAppReadyToFinish = false;
	private BottomBar mBottomBar;
	
	private WebView wv;
	private Color clr;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createStacks();
    }

    @Override
    public void onFragmentInteractionCallback(Bundle bundle) {
        String action = bundle.getString(Constants.ACTION);

        if (action != null) {
            switch (action) {
				/*
                case HomeFragment.ACTION_DASHBOARD:
                    showFragment(bundle, new DashboardFragment());
                    break;
                case DashboardFragment.ACTION_NOTIFICATION:
                    showFragment(bundle, new NotificationsFragment());
                    break;
                case NotificationsFragment.ACTION_DASHBOARD:
                    showFragment(bundle, new DashboardFragment());
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        resolveBackPressed();
    }

    private void createStacks() {
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
		mBottomBar.setActiveTabColor(clr.parseColor("#1b75bb"));
		mBottomBar.setTabSelectionInterceptor(this);
        
		
		//binder.bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        mainPageFragment = new MainPageFragment();
        discountFragment = new DiscountFragment();
        catalogFragment = new CatalogFragment();	
		cabinetFragment = new CabinetFragment();
        infoFragment = new InfoFragment();
		
		
        stacks = new LinkedHashMap<>();
        stacks.put(Constants.TAB_MAIN_PAGE_FRAGMENT, new Stack<>());
        stacks.put(Constants.TAB_DISCOUNT_FRAGMENT, new Stack<>());
        stacks.put(Constants.TAB_CATALOG_FRAGMENT, new Stack<>());
		stacks.put(Constants.TAB_CABINET_FRAGMENT, new Stack<>());
        stacks.put(Constants.TAB_INFO_FRAGMENT, new Stack<>());
		
        menuStacks = new ArrayList<>();
        menuStacks.add(Constants.TAB_MAIN_PAGE_FRAGMENT);
        
        stackList = new ArrayList<>();
        stackList.add(Constants.TAB_MAIN_PAGE_FRAGMENT);
        stackList.add(Constants.TAB_DISCOUNT_FRAGMENT);
        stackList.add(Constants.TAB_CATALOG_FRAGMENT);
        stackList.add(Constants.TAB_CABINET_FRAGMENT);
        stackList.add(Constants.TAB_INFO_FRAGMENT);
		
		
        mBottomBar.selectTabWithId(R.id.tab_main_page);
		//selectedTab(Constants.TAB_MAIN_PAGE_FRAGMENT);
    }

    
	
	
	private void selectedTab(String tabId) {

        currentTab = tabId;
        BaseFragment.setCurrentTab(currentTab);

        if (stacks.get(tabId).size() == 0) {
            /*
             * First time this tab is selected. So add first fragment of that tab.
             * We are adding a new fragment which is not present in stack. So add to stack is true.
             
		
            switch (tabId) {
                case Constants.TAB_MAIN_PAGE_FRAGMENT:
                    FragmentUtils.addInitialTabFragment(getSupportFragmentManager(), stacks, Constants.TAB_MAIN_PAGE_FRAGMENT, mainPageFragment, R.id.main_content_frame, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(mainPageFragment);
                    break;
                case Constants.TAB_DISCOUNT_FRAGMENT:
                    FragmentUtils.addInitialTabFragment(getSupportFragmentManager(), stacks, Constants.TAB_DISCOUNT_FRAGMENT, discountFragment, R.id.main_content_frame, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(discountFragment);
                    break;
					
				case Constants.TAB_CATALOG_FRAGMENT:
                    FragmentUtils.addInitialTabFragment(getSupportFragmentManager(), stacks, Constants.TAB_CATALOG_FRAGMENT, catalogFragment, R.id.main_content_frame, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(catalogFragment);
                    break;
				case Constants.TAB_CABINET_FRAGMENT:
                    FragmentUtils.addInitialTabFragment(getSupportFragmentManager(), stacks, Constants.TAB_CABINET_FRAGMENT, cabinetFragment, R.id.main_content_frame, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(cabinetFragment);
                    break;
				case Constants.TAB_INFO_FRAGMENT:
                    FragmentUtils.addInitialTabFragment(getSupportFragmentManager(), stacks, Constants.TAB_INFO_FRAGMENT, infoFragment, R.id.main_content_frame, true);
                    resolveStackLists(tabId);
                    assignCurrentFragment(infoFragment);
                    break;
            }
        } else {
            /*
             * We are switching tabs, and target tab already has at least one fragment.
             * Show the target fragment
             *
            FragmentUtils. showHideTabFragment(getSupportFragmentManager(), stacks.get(tabId).lastElement(), currentFragment);
            resolveStackLists(tabId);
            assignCurrentFragment(stacks.get(tabId).lastElement());
        }
    }

    private void popFragment() {
        /*e
         * Select the second last fragment in current tab's stack,
         * which will be shown after the fragment transaction given below
         *
        Fragment fragment = stacks.get(currentTab).elementAt(stacks.get(currentTab).size() - 2);

        /*pop current fragment from stack *
        stacks.get(currentTab).pop();

        FragmentUtils. removeFragment(getSupportFragmentManager(), fragment, currentFragment);

        assignCurrentFragment(fragment);
    }

    private void resolveBackPressed() {
        int stackValue = 0;
        if (stacks.get(currentTab).size() == 1) {
            Stack<Fragment> value = stacks.get(stackList.get(1));
            if (value.size() > 1) {
                stackValue = value.size();
                popAndNavigateToPreviousMenu();
            }
            if (stackValue <= 1) {
                if (menuStacks.size() > 1) {
                    navigateToPreviousMenu();
                } else {
                    finish();
                }
            }
        } else {
            popFragment();
        }
    }

    /*Pops the last fragment inside particular tab and goes to the second tab in the stack*
    private void popAndNavigateToPreviousMenu() {
        String tempCurrent = stackList.get(0);
        currentTab = stackList.get(1);
        BaseFragment.setCurrentTab(currentTab);
        mBottomBar.selectTabWithId(resolveTabPositions(currentTab));
        FragmentUtils. showHideTabFragment(getSupportFragmentManager(), stacks.get(currentTab).lastElement(), currentFragment);
        assignCurrentFragment(stacks.get(currentTab).lastElement());
        StackListManager. updateStackToIndexFirst(stackList, tempCurrent);
        menuStacks.remove(0);
    }

    private void navigateToPreviousMenu() {
        menuStacks.remove(0);
        currentTab = menuStacks.get(0);
        BaseFragment.setCurrentTab(currentTab);
       // mBottomBar.selectTabWithId(resolveTabPositions(currentTab));
        FragmentUtils. showHideTabFragment(getSupportFragmentManager(), stacks.get(currentTab).lastElement(), currentFragment);
        assignCurrentFragment(stacks.get(currentTab).lastElement());
    }

    /*
     * Add a fragment to the stack of a particular tab
     *
    private void showFragment(Bundle bundle, Fragment fragmentToAdd) {
        String tab = bundle.getString(Constants.DATA_KEY_1);
        boolean shouldAdd = bundle.getBoolean(Constants.DATA_KEY_2);
        FragmentUtils. addShowHideFragment(getSupportFragmentManager(), stacks, tab, fragmentToAdd, getCurrentFragmentFromShownStack(), R.id.main_content_frame, shouldAdd);
        assignCurrentFragment(fragmentToAdd);
    }

    private int resolveTabPositions(String currentTab) {
        int tabIndex = 0;
        switch (currentTab) {
            case Constants.TAB_MAIN_PAGE_FRAGMENT:
                tabIndex = R.id.tab_main_page;
                break;
            case Constants.TAB_DISCOUNT_FRAGMENT:
                tabIndex = R.id.tab_discount;
                break;
            case Constants.TAB_CATALOG_FRAGMENT:
                tabIndex = R.id.tab_catalog;
                break;
			case Constants.TAB_CABINET_FRAGMENT:
                tabIndex = R.id.tab_cabinet;
                break;
            case Constants.TAB_DISCOUNT_FRAGMENT:
                tabIndex = R.id.tab_info;
                break;
        }
        return tabIndex;
    }

    private void resolveStackLists(String tabId) {
        StackListManager. updateStackIndex(stackList, tabId);
        StackListManager. updateTabStackIndex(menuStacks, tabId);
    }

    private Fragment getCurrentFragmentFromShownStack() {
        return stacks.get(currentTab).elementAt(stacks.get(currentTab).size() - 1);
    }

    private void assignCurrentFragment(Fragment current) {
        currentFragment = current;
    }
	/*
	private static synchronized MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
  
        setContentView(R.layout.main);

//        if (BuildConfig.DEBUG) {
//            // Only debug properties, used for checking image memory management.
//            Picasso.with(this).setIndicatorsEnabled(true);
//            Picasso.with(this).setLoggingEnabled(true);
//        }

        // Prepare toolbar and navigation drawer
        //Toolbar toolbar = findViewById(R.id.main_toolbar);
        //setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();
        //if (actionBar != null) {
           // actionBar.setDisplayShowHomeEnabled(true);
       // } else {
            //Timber.e(new RuntimeException(), "GetSupportActionBar returned null.");
        //}
		Color clr = new Color();
		
        
		mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
		mBottomBar.setActiveTabColor(clr.parseColor("#1b75bb"));
		
		
		if(savedInstanceState != null){
			//Parcelable frIdArray = savedInstanceState.get("FragmentList");
			//FragmentManager fm = getSupportFragmentManager();
			//Fragment tt = fm.getFragment(savedInstanceState, "fr1");
			/*for(int id : frIdArray){
				
				fr.add((BaseFragment)(getSupportFragmentManager().findFragmentById(id)));
			}
			mBottomBar.onRestoreInstanceState(savedInstanceState.getParcelable("bottomBar"));
		}else{
			addInitialFragment();
		}
		
		//mBottomBar.setOnItemSelectedListener
		
		mBottomBar.setTabSelectionInterceptor(new TabSelectionInterceptor() {
				@Override
				public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId) {

					switch(newTabId){
						case R.id.tab_main_page:
							onMainPageSelected();
							break;
						case R.id.tab_discount:
							onDiscountSelected();
							break;
						case R.id.tab_catalog:
							onCatalogSelected();
							break;	
						case R.id.tab_cabinet:
							onCabinetSelected();
							break;
						case R.id.tab_info:
							onInfoSelected();
							break;
					}
					return false;
				}
			});
		mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
				@Override
				public void onTabSelected(@IdRes int tabId) {
					/*switch(tabId){
						case R.id.tab_main_page:
							onMainPageSelected();
							break;
						case R.id.tab_catalog:
							onCatalogSelected();
							break;
						
					}
					//messageView.setText(TabMessage.get(tabId, false));
				}
			});
			
		
		/*
        // Opened by notification with some data
        if (this.getIntent() != null && this.getIntent().getExtras() != null) {
            String target = this.getIntent().getExtras().getString(CONST.BUNDLE_PASS_TARGET, "");
            String title = this.getIntent().getExtras().getString(CONST.BUNDLE_PASS_TITLE, "");
            Timber.d("Start notification with banner target: %s", target);

            Banner banner = new Banner();
            banner.setTarget(target);
            banner.setName(title);
            onBannerSelected(banner);

            Analytics.logOpenedByNotification(target);
        }
		
		wv = new WebView(getApplicationContext());

		wv.setWebViewClient(new MyWebViewClient());
		wv.setWebChromeClient(new MyWebChromeClient(getInstance()));
		// включаем поддержку JavaScript
		wv.getSettings().setDomStorageEnabled(true);
		wv.getSettings().setJavaScriptEnabled(true);
		// указываем страницу загрузки
		wv.loadUrl("https://www.chitai-gorod.ru/"); 
		
		
		
		
    }
	
	

	
    /**
     * Add first fragment to the activity. This fragment will be attached to the bottom of the fragments stack.
     * When fragment stack is cleared {@link #clearBackStack}, this fragment will be shown.
     
    private void addInitialFragment() {
		currentFragment = "MainPageFragment";
		
        BaseFragment fragment = new MainPageFragment();
		
		fragment.BACK_STACK_TAG = currentFragment;
        FragmentManager frgManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
        fragmentTransaction.add(R.id.main_content_frame, fragment).commit();
        frgManager.executePendingTransactions();
    }
	

    /**
     * Method creates fragment transaction and replace current fragment with new one.
     *
     * @param newFragment    new fragment used for replacement.
     * @param transactionTag text identifying fragment transaction.
     
    private void replaceFragment(BaseFragment newFragment, String transactionTag) {
        
		if (newFragment != null) {
			currentFragment = transactionTag;
			switch (currentFragment){
				case "MainPageFragment":
					mBottomBar.selectTabWithId(R.id.tab_main_page);
					break;
				case "DiscountFragment":
					mBottomBar.selectTabWithId(R.id.tab_discount);
					break;
				case "CatalogFragment":
					mBottomBar.selectTabWithId(R.id.tab_catalog);
					break;
				case "CabinetFragment":
					mBottomBar.selectTabWithId(R.id.tab_cabinet);
					break;
				case "InfoFragment":
					mBottomBar.selectTabWithId(R.id.tab_info);
					break;
			}
			newFragment.BACK_STACK_TAG = transactionTag;
            FragmentManager frgManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
            fragmentTransaction.setAllowOptimization(false);
            fragmentTransaction.addToBackStack(transactionTag);
            //fr.add(newFragment);
			fragmentTransaction.replace(R.id.main_content_frame, newFragment).commit();
            frgManager.executePendingTransactions();
        } else {
            //Timber.e(new RuntimeException(), "Replace fragments with null newFragment parameter.");
        }
    }
	private void replaceFragmentNoBackStack(Fragment newFragment) {
        
		//BaseFragment newFragment = (BaseFragment) frgManager.findFragmentByTag(newFragmentTag);
		if (newFragment != null) {
			//currentFragment = transactionTag;
			//newFragment.BACK_STACK_TAG = transactionTag;
            FragmentManager frgManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
            fragmentTransaction.setAllowOptimization(false);
           // fragmentTransaction.addToBackStack("fr");
            fragmentTransaction.replace(R.id.main_content_frame, newFragment).commit();
            frgManager.executePendingTransactions();
        } else {
            //Timber.e(new RuntimeException(), "Replace fragments with null newFragment parameter.");
        }
    }
	
    /**
     * Method clear fragment backStack (back history). On bottom of stack will remain Fragment added by {@link #addInitialFragment()}.
     
    private void clearBackStack() {
        // Timber.d("Clearing backStack");
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            if (BuildConfig.DEBUG) {
                for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                    //Timber.d("BackStack content_%d= id: %d, name: %s", i, manager.getBackStackEntryAt(i).getId(), manager.getBackStackEntryAt(i).getName());
                }
            }
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //Timber.d("backStack cleared.");
//        TODO maybe implement own fragment backStack handling to prevent banner fragment recreation during clearing.
//        http://stackoverflow.com/questions/12529499/problems-with-android-fragment-back-stack
    }
	
	
	
	public void get(String req) {
		if(wv != null){
			wv.loadUrl(req);
		}
	}

	public void onMainPageSelected(){
		currentFragment = "MainPageFragment";
		FragmentManager fm = getSupportFragmentManager();
		List fr = new ArrayList<BaseFragment>();
		int countF = fm.getBackStackEntryCount();
		
		for(int i = 0 ; i < countF; i++){
			fr.add(fm.getBackStackEntryAt(i));
		}
		
		boolean temp = true;
		
		for(int i = (fr.size()-1); temp && i >= 0  ;i-- ){
			if(((BaseFragment)fr.get(i)).BACK_STACK_TAG.equals("MainPageFragment")){
				temp = !temp;
				replaceFragmentNoBackStack((BaseFragment)fr.get(i));
				//fm.popBackStackImmediate("MainPageFragment", 0);
			}
		}
		
		if(temp){
			BaseFragment frg = new MainPageFragment();
			replaceFragment(frg, "MainPageFragment");
		}
	}
	public void onDiscountSelected(){
		FragmentManager fm = getSupportFragmentManager();
		List fr = new ArrayList<BaseFragment>();
		int countF = fm.getBackStackEntryCount();

		for(int i = 0 ; i < countF; i++){
			fr.add(fm.getBackStackEntryAt(i));
		}

		boolean temp = true;
		for(int i = (fr.size()-1); temp && i >= 0  ;i-- ){		
			if(((BaseFragment)fm.findFragmentById(((FragmentManager.BackStackEntry) fr.get(i)).getId())).BACK_STACK_TAG.equals("DiscountFragment")){
				temp = !temp;
				replaceFragmentNoBackStack((BaseFragment)fr.get(i));
				//fm.popBackStackImmediate("CatalogFragment" , 1);
			}
		}
		if(temp){
			BaseFragment frg = new DiscountFragment();
			replaceFragment(frg,  "DiscountFragment");		
		}
	}
	
	public void onCatalogSelected(){
		FragmentManager fm = getSupportFragmentManager();
		List fr = new ArrayList<FragmentManager.BackStackEntry>();
		int countF = fm.getBackStackEntryCount();

		for(int i = 0 ; i < countF; i++){
			fr.add(fm.getBackStackEntryAt(i));
		}
		
		boolean temp = true;
		for(int i = (fr.size()-1); temp && i >= 0  ;i-- ){		
			FragmentManager.BackStackEntry currEntry = (FragmentManager.BackStackEntry) fr.get(i);
			int currId = currEntry.getId();
			BaseFragment myFragment = (BaseFragment) fm.get);
			if(myFragment.BACK_STACK_TAG.equals("CatalogFragment")){
				temp = !temp;
				replaceFragmentNoBackStack((BaseFragment)fr.get(i));
				//fm.popBackStackImmediate("CatalogFragment" , 1);
			}
		}
		if(temp)
			replaceFragment(new CatalogFragment(), "CatalogFragment");

	}
	public void onCabinetSelected(){
		FragmentManager fm = getSupportFragmentManager();
		List fr = new ArrayList<BaseFragment>();
		int countF = fm.getBackStackEntryCount();

		for(int i = 0 ; i < countF; i++){
			fr.add(fm.getBackStackEntryAt(i));
		}
		
		boolean temp = true;
		for(int i = fr.size()-1; temp && i >= 0  ;i-- ){
			if(((BaseFragment)fr.get(i)).BACK_STACK_TAG.equals("CabinetFragment")){
				temp = !temp;
				replaceFragmentNoBackStack((BaseFragment)fr.get(i));
				//fm.popBackStackImmediate("CabinetFragment" , 0);
			}
		}
		if(temp)
			replaceFragment(new CabinetFragment(), "CabinetFragment");
	}
	
	public void onInfoSelected(){	
		FragmentManager fm = getSupportFragmentManager();
		List fr = new ArrayList<BaseFragment>();
		int countF = fm.getBackStackEntryCount();

		for(int i = 0 ; i < countF; i++){
			fr.add(fm.getBackStackEntryAt(i));
		}
		
		boolean temp = true;
		for(int i = fr.size()-1; temp && i >= 0  ;i-- ){
			if(((BaseFragment)fr.get(i)).BACK_STACK_TAG.equals("InfoFragment")){
				temp = !temp;
				replaceFragmentNoBackStack((BaseFragment)fr.get(i));
				//fm.popBackStackImmediate("InfoFragment" , 0);
			}
		}
		if(temp)
			replaceFragment(new InfoFragment(), "InfoFragment");
	}
	
	
	public void onCartSelected(){
		FragmentManager fm = getSupportFragmentManager();
		List fr = new ArrayList<BaseFragment>();
		int countF = fm.getBackStackEntryCount();

		for(int i = 0 ; i < countF; i++){
			fr.add(fm.getBackStackEntryAt(i));
		}
		
		boolean temp = true;
		for(int i = fr.size()-1; temp && i >= 0  ;i-- ){
			if(((BaseFragment)fr.get(i)).BACK_STACK_TAG.equals("CartFragment")){
				temp = !temp;
				replaceFragmentNoBackStack((BaseFragment)fr.get(i));
				//fm.popBackStackImmediate("CartFragment" , 0);
			}
		}
		if(temp)
			replaceFragment(new CartFragment(), "CartFragment");
	}
	
	public void onGoFSearch(String tag, BaseFragment frg){	
		replaceFragment(frg, tag);
	}

	@Override
	public void onBackPressed()
	{
		
		FragmentManager fm = getSupportFragmentManager();
		//List fr = fm.getFragments();
		
		//int _id = getIdLastFragment();
	
		//FragmentManager.BackStackEntry bse ;
		
		

		int countF = fm.getBackStackEntryCount();
		
		
		/*
		if(getCountFragment() > 1){	
			fr.remove(getIdLastFragment());
			replaceFragmentNoBackStack(fr.get(getIdLastFragment()));
		}else{
			int s = getCountFragment();
			
			if(isAppReadyToFinish && s == 1){
				finish();
			}else{
				
				Toast.makeText(getApplicationContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
				isAppReadyToFinish = true;
			new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isAppReadyToFinish = false;
                    }
                }, 2000);
			}
		}
		
		//super.onBackPressed();
	}

/*
	
	private int getCountFragment(){
		int s = 0;
		for(int i = 0; i<fr.size()  ;i++ ){
			if(fr.get(i).BACK_STACK_TAG.equals(currentFragment)){
				s++;
			}
		}
		return s;
	}
	private int getIdLastFragment(){
		int _id = 0;
		for(int i = 0; i<fr.size()  ;i++ ){
			if(((BaseFragment)fr.get(i)).BACK_STACK_TAG.equals(currentFragment)){
				_id = i;
				//replaceFragmentNoBackStack((BaseFragment)fr.get(i));
				//fm.popBackStackImmediate("CartFragment" , 0);
			}
		}
		return _id;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		
		/*
		int[] frIdArray = new int[fr.size()];
		for(int i = 0; i < fr.size(); i++){
			frIdArray[i] = ( fr.get(i)).getId();
		}
		//outState.putIntArray("FragmentList", frIdArray);
		//outState.putParcelable("FragmentList", (Parcelable)fr);
		//for(int i = 0; i < fr.size(); i++)
			//getSupportFragmentManager().putFragment(outState, "fr"+i, (Fragment)fr.get(i));
		//outState.putParcelableArrayList("yy", fr);
		outState.putParcelable("bottomBar", mBottomBar.onSaveInstanceState());
	}

	
	
	
	*/

    
	

		
	
	
}
