package co.edu.unisimon.bienestar.business.http.controller;

import co.edu.unisimon.bienestar.business.domain.constant.Message;
import co.edu.unisimon.bienestar.common.TokenUtil;
import lombok.AllArgsConstructor;

/**
 * @author William Torres
 * @version 1.0
 */
@AllArgsConstructor
public class Controller {

    protected final Message message;
    protected final TokenUtil tokenUtil;

}

