package org.xobo.dorado.mybatis.pagehelper;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.helper.MySqlDialect;

public class StepsMySQLDialect extends MySqlDialect {

  private static ThreadLocal<String> skipThreadLocal = new ThreadLocal<>();

  private static String SKIP_COUNT = "SKIP_COUNT";
  private static String SKIP_QUERY = "SKIP_QUERY";
  private static long DEFAULT_PAGES = 10;

  public static void skipCount() {
    skipThreadLocal.set(SKIP_COUNT);
  }

  public static void skipQuery() {
    skipThreadLocal.set(SKIP_QUERY);
  }

  private static boolean shouldSkipCount() {
    return SKIP_COUNT.equals(skipThreadLocal.get());
  }

  private static boolean shouldSkipQuery() {
    return SKIP_QUERY.equals(skipThreadLocal.get());
  }

  @Override
  public boolean beforeCount(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
    return super.beforeCount(ms, parameterObject, rowBounds) && !shouldSkipCount();
  }

  @Override
  public boolean afterCount(long count, Object parameterObject, RowBounds rowBounds) {
    return super.afterCount(count, parameterObject, rowBounds) && !shouldSkipQuery();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
    Object object = super.afterPage(pageList, parameterObject, rowBounds);

    if (shouldSkipCount()) {
      Page page = getLocalPage();
      if (page != null) {
        page.setTotal(
            page.size() < page.getPageSize() ? calcTotalCount(page) : predictTotalCount(page, parameterObject));
      }
    }

    skipThreadLocal.remove();
    return object;
  }

  /**
   * @param page
   * @return
   */
  Long calcTotalCount(Page page) {
    return (long) ((page.size() * (page.getPageNum() - 1)) + page.size());
  }

  /**
   * @param page
   * @param parameterObject
   * @return
   */
  @SuppressWarnings("rawtypes")
  Long predictTotalCount(Page page, Object parameterObject) {
    Long predictTotalCount = null;
    if (parameterObject instanceof Map) {
      predictTotalCount = MapUtils.getLongValue((Map) parameterObject, "COUNT_TOTAL");
    }
    if (predictTotalCount == null) {
      predictTotalCount = page.getPageSize() * DEFAULT_PAGES;
    }
    return predictTotalCount;
  }

}
