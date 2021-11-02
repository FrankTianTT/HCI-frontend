package com.huawei.courselearningdemo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.huawei.courselearningdemo.ui.activity.MainActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SimpleTextSearchTest {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testSearch() throws InterruptedException {
        // 在HomeFragment顶部的搜索栏里 输入“Java”
        onView(withId(R.id.search_course)).perform(typeText("Java"));
        Thread.sleep(1000);
        // 按下搜索按钮后 等待后端返回数据
        onView(withId(R.id.search_course)).perform(pressImeActionButton());
        Thread.sleep(2000);
        // 数据加载完成后，开始判断当前的 RecyclerView内列表元素项 是否符合预期
        // 尝试将屏幕滚动至包含预期的字符串的元素项
        // 当匹配不到相应的元素项时，scrollTo操作会失败
        onView(ViewMatchers.withId(R.id.course_list_rv))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(R.string.course_name_for_test_1))
                ));
        onView(ViewMatchers.withId(R.id.course_list_rv))
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText(R.string.course_name_for_test_2))
                ));
    }
}
