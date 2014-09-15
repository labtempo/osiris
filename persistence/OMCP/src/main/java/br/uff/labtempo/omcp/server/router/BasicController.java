/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.omcp.server.router;

import br.uff.labtempo.omcp.server.router.annotations.RequestMapping;
import br.uff.labtempo.omcp.common.RequestMethod;

/**
 *
 * @author Felipe
 */
public class BasicController {

    @RequestMapping(value = "/controller/:id", method = RequestMethod.GET)
    public String index(String id) {
        return "teste";
    }
}
