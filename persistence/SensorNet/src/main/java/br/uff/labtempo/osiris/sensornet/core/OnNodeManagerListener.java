/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.labtempo.osiris.sensornet.core;

/**
 *
 * @author Felipe
 */
interface OnNodeManagerListener {
    void onNodeChange(Node node, EventType type);
}