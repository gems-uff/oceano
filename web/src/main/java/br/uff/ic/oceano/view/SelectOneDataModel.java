/*
 * SelectOneDataModel.java
 *
 * Created on 15 de Outubro de 2007, 14:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package br.uff.ic.oceano.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author thiago
 */
public class SelectOneDataModel<T> {

    public static final String TEXTO_SELECIONE_AQUI = "Selecione aqui";
    private String selecao;
    private List<SelectItem> listaSelecao;

    public static <T> SelectOneDataModel<T> criaSemTextoInicial(final Collection<T> itens) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size());

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }

        select.setListaSelecao(listaSelecao);

        return select;
    }

    /**
     * O texto default no comboBox é: Selecione aqui
     * @param <T>
     * @param itens
     * @return
     */
    public static <T> SelectOneDataModel<T> criaComTextoInicialDefault(final Collection<T> itens) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }

        select.setSelecao(TEXTO_SELECIONE_AQUI);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    public static <T> SelectOneDataModel<T> criaComTextoInicialPersonalizado(Collection<T> itens, final String LabelPrimeiroElemento) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        listaSelecao.add(new SelectItem(null, LabelPrimeiroElemento));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
        String selecao = LabelPrimeiroElemento;

        select.setSelecao(selecao);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    public static <T> SelectOneDataModel<T> criaComObjetoSelecionado(Collection<T> itens, T objetoSelecionado) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
        String selecao = objetoSelecionado.toString();

        select.setSelecao(selecao);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    public static <T> SelectOneDataModel<T> criaComObjetoSelecionadoSemTextoInicial(Collection<T> itens, T objetoSelecionado) {
        SelectOneDataModel<T> select = new SelectOneDataModel<T>();
        List<SelectItem> listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
        String selecao = objetoSelecionado.toString();

        select.setSelecao(selecao);
        select.setListaSelecao(listaSelecao);

        return select;
    }

    public SelectOneDataModel() {
        listaSelecao = new ArrayList<SelectItem>(1);
        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));
        selecao = TEXTO_SELECIONE_AQUI;
    }

    public SelectOneDataModel(Collection<T> itens) {
        if (itens != null) {
            listaSelecao = new ArrayList<SelectItem>(itens.size() + 1);
        } else {
            listaSelecao = new ArrayList<SelectItem>(1);
        }

        listaSelecao.add(new SelectItem(null, TEXTO_SELECIONE_AQUI));

        for (Object elem : itens) {
            listaSelecao.add(new SelectItem(elem, elem.toString()));
        }
    }

    public String getSelecao() {
        return selecao;
    }

    public void setSelecao(String selecao) {
        this.selecao = selecao;
    }

    public List<SelectItem> getListaSelecao() {
        return listaSelecao;
    }

    public void setListaSelecao(List<SelectItem> listaSelecao) {
        this.listaSelecao = listaSelecao;
    }

    public T getObjetoSelecionado() {
        if (selecao == null) {
            return null;
        }
        for (SelectItem item : listaSelecao) {
            if (item.getLabel().equals(selecao)) {
                return (T) item.getValue();
            }
        }
        return null;
    }

    public int getQuantidadeElementos() {
        int qtd = listaSelecao.size() - 1;
        return (qtd > 0) ? qtd : 0;
    }

    public boolean isTextoInicialSelecionado() {
        if (selecao == null) {
            return true;
        }
        return false;
    }
}
