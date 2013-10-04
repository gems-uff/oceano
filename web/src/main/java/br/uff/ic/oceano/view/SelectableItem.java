/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.view;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class SelectableItem<T> {

    private boolean selected = false;
    private T item;

    public SelectableItem(T item) {
        this.item = item;
    }

    public SelectableItem(T item, boolean selected) {
        this.item = item;
        this.selected = selected;
    }



    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the item
     */
    public <T> T getItem() {
        return (T) item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(T item) {
        this.item = item;
    }
    /**
     *
     * @param List<T>
     * @return uma lista de SelectableItem do tipo T  a partir de uma lista qualquer
     */
    public static <T> List<SelectableItem<T>> getListaSelecao(List<T> listaSelecao){
        final List<SelectableItem<T>> lista = new ArrayList(listaSelecao.size());
        for (T obj : listaSelecao) {
            lista.add(new SelectableItem(obj));
        }
        return lista;
    }

    /**
     *
     * @param List de SelectableItem tipo T
     * @return retorna um (List de SelectableItem tipo T) com os selecionados
     */
    public static <T> List<SelectableItem<T>> getListaSelecaoSelecionados(List<SelectableItem<T>> listaSelecao){
        final List<SelectableItem<T>> lista = new ArrayList(listaSelecao.size());
        for (SelectableItem obj : listaSelecao) {
            if(obj.selected) lista.add(obj);
        }
        return lista;
    }

    /**
     *
     * @param List de SelectableItem tipo T
     * @return retorna um (List de T) dos objetos selecionados
     */
    public static <T> List<T> getListaObjetosSelecionados(List<SelectableItem<T>> listaSelecao){
        final List<T> lista = new ArrayList<T>(listaSelecao.size());
        for (SelectableItem<T> obj : listaSelecao) {
            if(obj.selected) lista.add(obj.item);
        }
        return lista;
    }

    /**
     *
     * @param List de SelectableItem tipo T
     * @return retorna um (List de T) completa dos objetos decorados
     */
    public static <T> List<T> getListaObjetosCompleta(List<SelectableItem<T>> listaSelecao){
        final List<T> lista = new ArrayList<T>(listaSelecao.size());
        for (SelectableItem<T> obj : listaSelecao) {
            lista.add(obj.item);
        }
        return lista;
    }

    /**
     *
     * @param T
     * @return faz o equals do objeto decorado
     */


    @Override
    public boolean equals(Object obj) {
        return item.equals(((SelectableItem<T>)obj).item);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.item != null ? this.item.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return item == null ? "Objeto NÃ£o Informado" : item.toString();
    }

    
}
