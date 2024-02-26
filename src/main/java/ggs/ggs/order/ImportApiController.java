package ggs.ggs.order;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

@Controller
public class ImportApiController {
    private IamportClient api;

    public ImportApiController() {
        this.api = new IamportClient("6086320825272622", "4hbwIToN56LcZasvbNsRQJWtZ0qeaowDDVFmEwaWwBg5BakVUT6Ihrw71JPCUE3M3vmFhxZxpCrmVH64");

    }

    @ResponseBody
    @RequestMapping(value = "/verifyIamport/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(Model model, Locale locale, HttpSession session
            , @PathVariable(value = "imp_uid") String imp_uid) throws IamportResponseException, IOException {

        return api.paymentByImpUid(imp_uid);
    }

    @RequestMapping(value="/orderCompleteMobile", produces = "application/text; charset=utf8", method = RequestMethod.GET)
    public String orderCompleteMobile(
            @RequestParam(required = false) String imp_uid
            , @RequestParam(required = false) String merchant_uid
            , Model model
            , Locale locale
            , HttpSession session) throws IamportResponseException, IOException
    {

        IamportResponse<Payment> result = api.paymentByImpUid(imp_uid);

        // 결제 가격과 검증결과를 비교한다.
        if(result.getResponse().getAmount().compareTo(BigDecimal.valueOf(100)) == 0) {
            System.out.println("검증통과");
        }

        return "home";
    }
}
