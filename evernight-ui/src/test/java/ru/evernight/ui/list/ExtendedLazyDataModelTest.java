package ru.evernight.ui.list;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.evernight.dao.statement.CrudStatements;
import ru.evernight.dao.statement.filter.DbFilter;
import ru.evernight.ui.filters.UiFIlter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ExtendedLazyDataModelTest {

    @Test
    public void testFilters() {
        UiFIlter f = Mockito.mock(UiFIlter.class);
        DbFilter dbFilter = Mockito.mock(DbFilter.class);
        Mockito.when(f.covert()).thenReturn(dbFilter);
        MyLazyDataModel sut = new MyLazyDataModel(f, null, new Date());

        List result = sut.filters();
        System.out.println(result);
        Assert.assertEquals(result.get(0), dbFilter);
        Assert.assertEquals(result.size(), 1);
    }

    @AllArgsConstructor
    private static class MyLazyDataModel extends ExtendedLazyDataModel {
        @Getter
        private UiFIlter uiFIlter;
        private UiFIlter uiFIlter2;
        private Date d;

        @Override
        protected CrudStatements getMainStatements() {
            return null;
        }
    }
}