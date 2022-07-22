import com.transaction.enums.MatchType;
import com.transaction.models.Buyer;
import com.transaction.models.Entity;
import com.transaction.models.MatchScore;
import com.transaction.models.Supplier;
import com.transaction.serviceImpl.EntityMatchingServiceImpl;
import com.transaction.utils.Constants;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.testng.AssertJUnit.assertEquals;

@Test
public class EntityMatchingServiceImplTest {


    public void stringMatchingTest() throws IllegalAccessException {
        EntityMatchingServiceImpl entityMatchingService = new EntityMatchingServiceImpl();
        Entity buyer = new Buyer();
        Entity supplier = new Supplier();
        buyer.setBillNo("ABC");
        supplier.setBillNo("DEF");
        Field[] fields = Entity.class.getDeclaredFields();
        MatchScore expected = new MatchScore(MatchType.ONLY_IN_BUYER, 0);
        for(Field field: fields) {
            field.setAccessible(true);
            if(field.getName().equalsIgnoreCase(Constants.BILL_NO)) {
                MatchScore matchScore = entityMatchingService.matchStringFields(buyer, supplier, new MatchScore(null, 0), field,2);
                assertEquals(matchScore.getMatchType(), expected.getMatchType());
            }
        }
    }


}
