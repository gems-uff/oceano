/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller;

import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.model.Estado;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import br.uff.ic.oceano.ourico.service.EstadoService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author marapao
 */
public class OuricoBean extends BaseBean {

    private String PAGINA_LISTA_CHECKOUTS = "def:/privado/ourico/autobranch/autobranchesList";
    private String PAGINA_DETAIL_CHECKOUT = "def:/privado/ourico/autobranch/autobranchDetail";
    private String PAGINA_AUTOBRANCH_NAO_VERIFICADO = "def:/privado/ourico/autobranch/semVerificacao";
    private String PAGINA_DETAIL_CONFLICT = "def:/privado/ourico/autobranch/conflictDetail";

    private Estado estado = new Estado();
    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);
    private List<Estado> estados = new ArrayList<Estado>();
    private DataModel tableEstados;
    private Estado estadoSelecionado = new Estado();


    private CheckOut checkout = new CheckOut();
    private CheckOut checkOutSelecionado;
    private CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
    private List<CheckOut> checkouts = new ArrayList<CheckOut>();
    private DataModel tableAutobranches;

    private String autobranch;

    public OuricoBean() {
        super("OuricoBean");
    }

    /**
     * @return the PAGINA_LISTA_CHECKOUTS
     */
    public String getPAGINA_LISTA_CHECKOUTS() {
        return PAGINA_LISTA_CHECKOUTS;
    }

    /**
     * @param PAGINA_LISTA_CHECKOUTS the PAGINA_LISTA_CHECKOUTS to set
     */
    public void setPAGINA_LISTA_CHECKOUTS(String PAGINA_LISTA_CHECKOUTS) {
        this.PAGINA_LISTA_CHECKOUTS = PAGINA_LISTA_CHECKOUTS;
    }

    /**
     * @return the estado
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    /**
     * @return the estadoService
     */
    public EstadoService getEstadoService() {
        return estadoService;
    }

    /**
     * @param estadoService the estadoService to set
     */
    public void setEstadoService(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    /**
     * @return the estados
     */
    public List<Estado> getEstados() {
        return estados;
    }

    /**
     * @param estados the estados to set
     */
    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    /**
     * @return the checkout
     */
    public CheckOut getCheckout() {
        return checkout;
    }

    /**
     * @param checkout the checkout to set
     */
    public void setCheckout(CheckOut checkout) {
        this.checkout = checkout;
    }

    /**
     * @return the checkOutService
     */
    public CheckOutService getCheckOutService() {
        return checkOutService;
    }

    /**
     * @param checkOutService the checkOutService to set
     */
    public void setCheckOutService(CheckOutService checkOutService) {
        this.checkOutService = checkOutService;
    }

    /**
     * @return the checkouts
     */
    public List<CheckOut> getCheckouts() {
        if (checkouts.size() == 0) {
            System.out.println("-----------------autobranches----------------------------------");
            checkouts = checkOutService.getAll();
            for (CheckOut checkOut : checkouts) {
                System.out.println(checkOut.getAutobranch());
            }
            System.out.println("-----------------fim autobranches----------------------------------");
        }
        return checkouts;
    }

    /**
     * @param checkouts the checkouts to set
     */
    public void setCheckouts(List<CheckOut> checkouts) {
        this.checkouts = checkouts;
    }

    /**
     * @return the tableAutobranches
     */
    public DataModel getTableAutobranches() {
        if (tableAutobranches == null) {
            List<CheckOut> checkOuts = checkOutService.getAll();

            for (CheckOut checkOut : checkOuts) {
                List<Estado> estados = estadoService.getByAutobranch(checkOut.getAutobranch());
                checkOut.setCurrentState(estados.get(estados.size() -1));
            }

            tableAutobranches = new ListDataModel(checkOuts);
        }

        return tableAutobranches;
    }

    /**
     * @param tableAutobranches the tableAutobranches to set
     */
    public void setTableAutobranches(DataModel tableAutobranches) {
        this.tableAutobranches = tableAutobranches;
    }

    public String pagina_lista_autobranches() {
        return PAGINA_LISTA_CHECKOUTS;
    }

    /**
     * @return the PAGINA_DETAIL_CHECKOUT
     */
    public String getPAGINA_DETAIL_CHECKOUT() {
        return PAGINA_DETAIL_CHECKOUT;
    }

    /**
     * @param PAGINA_DETAIL_CHECKOUT the PAGINA_DETAIL_CHECKOUT to set
     */
    public void setPAGINA_DETAIL_CHECKOUT(String PAGINA_DETAIL_CHECKOUT) {
        this.PAGINA_DETAIL_CHECKOUT = PAGINA_DETAIL_CHECKOUT;
    }

    public String pagina_Detail_Checkout() {
        return PAGINA_DETAIL_CHECKOUT;
    }

    public String detailAutobranch() {
        try {
            checkOutSelecionado = (CheckOut) tableAutobranches.getRowData();
            checkOutSelecionado = checkOutService.getbyAutobranch(checkOutSelecionado.getAutobranch());
            estados = estadoService.getByAutobranch(checkOutSelecionado.getAutobranch());
            tableEstados = new ListDataModel(estados);
            autobranch = checkOutSelecionado.getAutobranch().toString();
            
            if(estados.size() > 0)
                return PAGINA_DETAIL_CHECKOUT;
            else
                return PAGINA_AUTOBRANCH_NAO_VERIFICADO;
            
        } catch (ObjetoNaoEncontradoException ex) {
            Logger.getLogger(OuricoBean.class.getName()).log(Level.SEVERE, null, ex);
            return PAGINA_AUTOBRANCH_NAO_VERIFICADO;
        }

    }

    public String detailConflict(){

        setEstadoSelecionado((Estado) tableEstados.getRowData());

        return PAGINA_DETAIL_CONFLICT;
    }

    /**
     * @return the checkOutSelecionado
     */
    public CheckOut getCheckOutSelecionado() {
        return checkOutSelecionado;
    }

    /**
     * @param checkOutSelecionado the checkOutSelecionado to set
     */
    public void setCheckOutSelecionado(CheckOut checkOutSelecionado) {
        this.checkOutSelecionado = checkOutSelecionado;
    }

    /**
     * @return the tableEstados
     */
    public DataModel getTableEstados() {
        return tableEstados;
    }

    /**
     * @param tableEstados the tableEstados to set
     */
    public void setTableEstados(DataModel tableEstados) {
        this.tableEstados = tableEstados;
    }

    /**
     * @return the PAGINA_AUTOBRANCH_NAO_VERIFICADO
     */
    public String getPAGINA_AUTOBRANCH_NAO_VERIFICADO() {
        return PAGINA_AUTOBRANCH_NAO_VERIFICADO;
    }

    /**
     * @param PAGINA_AUTOBRANCH_NAO_VERIFICADO the PAGINA_AUTOBRANCH_NAO_VERIFICADO to set
     */
    public void setPAGINA_AUTOBRANCH_NAO_VERIFICADO(String PAGINA_AUTOBRANCH_NAO_VERIFICADO) {
        this.PAGINA_AUTOBRANCH_NAO_VERIFICADO = PAGINA_AUTOBRANCH_NAO_VERIFICADO;
    }

    /**
     * @return the autobranch
     */
    public String getAutobranch() {
        return autobranch;
    }

    /**
     * @param autobranch the autobranch to set
     */
    public void setAutobranch(String autobranch) {
        this.autobranch = autobranch;
    }

    /**
     * @return the PAGINA_DETAIL_CONFLICT
     */
    public String getPAGINA_DETAIL_CONFLICT() {
        return PAGINA_DETAIL_CONFLICT;
    }

    /**
     * @param PAGINA_DETAIL_CONFLICT the PAGINA_DETAIL_CONFLICT to set
     */
    public void setPAGINA_DETAIL_CONFLICT(String PAGINA_DETAIL_CONFLICT) {
        this.PAGINA_DETAIL_CONFLICT = PAGINA_DETAIL_CONFLICT;
    }

    /**
     * @return the estadoSelecionado
     */
    public Estado getEstadoSelecionado() {
        return estadoSelecionado;
    }

    /**
     * @param estadoSelecionado the estadoSelecionado to set
     */
    public void setEstadoSelecionado(Estado estadoSelecionado) {
        this.estadoSelecionado = estadoSelecionado;
    }
    
}
