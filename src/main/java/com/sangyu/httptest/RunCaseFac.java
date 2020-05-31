package com.sangyu.httptest;

import com.sangyu.dto.HttpTestDTO;
import com.sangyu.vo.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * 执行用例入口
 * User: pengyapan
 * Date: 2020/5/15
 * Time: 上午11:11
 */
public class RunCaseFac {

    private HttpTestDTO httpTestDTO;
    private HttpResponse httpResponse;

    public RunCaseFac(HttpTestDTO httpTestDTO) {
        this.httpTestDTO = httpTestDTO;
    }

    public HttpResponse execCase() throws IOException, URISyntaxException, SQLException, ClassNotFoundException {
        RunCase runCase = new RunCase(httpTestDTO);
        // 参数路径和前置动作都为空 请求无参数
        if (httpTestDTO.getParameters() == null && httpTestDTO.getBeforeAction() == null) {
            httpResponse = runCase.runCaseNoParameter();
            // 前置工作不为空，这种情况的场景：不需要传递参数，但是依赖sql的执行或请求的执行
        } else if (httpTestDTO.getParameters() == null && httpTestDTO.getBeforeAction() != null) {
            new CaseAction().replaceNoRes(httpTestDTO.getBeforeAction());
            httpResponse = runCase.runCaseNoParameter();
            // 参数路径不为空，前置动作可能为空或不为空，在执行参数化的时候去判断
        } else if (httpTestDTO.getParameters() != null) {
            httpResponse = runCase.runCaseWithParameter(runCase.getRequestParameters(httpTestDTO.getParameters()));
        }
        return httpResponse;
    }

}
