/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.controle;

/**
 *
 * @author marapao
 */
public final class ConstantesOurico {

    public static final String PROJETO_INICIO = "[OURIÇO] ";
    public static final String PROJETO_FIM = " [OURIÇO]\n";


    //estados em que um autobranch pode estar


    public static final String CHECKOUT_SUCESSO = "Checked-out";
    public static final String CHECKOUT_FALHA = "Check-out failed";


    //mensagens de passagem de estado
    public static final String ANALISE_SINTATICA_1_OK = "First level syntactic verification successfully performed.";
    public static final String ANALISE_SINTATICA_2_OK = "Second level syntactic verification successfully performed";
    public static final String ANALISE_SEMANTICA_1_OK = "First level semantic verification successfully performed.";
    public static final String ANALISE_SEMANTICA_2_OK = "Second level semantic verification successfully performed.";
    public static final String ANALISE_FISICA_2_OK = "Second level physic verification successfully performed";
    public static final String ANALISE_FISICA_INTEGRACAO_OK = "Asynchronous check-in successfully performed";
//    public static final String CICLO_COMPLETO = "As verificações foram realizadas com sucesso";
    
    public static final String ANALISE_SINTATICA_1_FAIL = "First level syntactic verification failed.";
    public static final String ANALISE_SINTATICA_2_FAIL = "Second level syntactic verification failed.";
    public static final String ANALISE_SEMANTICA_1_FAIL = "First level semantic verification failed.";
    public static final String ANALISE_SEMANTICA_2_FAIL = "Second level semantic verification failed.";
    public static final String ANALISE_FISICA_2_FAIL = "Second level physic verification failed";
    public static final String ANALISE_FISICA_INTEGRACAO_FAIL = "Asynchronous check-in failed";

    public static final String FILTRO_INFORMATIVO_SINTATICO_OK = "Syntactic analysis informative successfully performed.";
    public static final String FILTRO_INFORMATIVO_SINTATICO_FAIL = "Syntactic analysis informative unsuccessfully performed.";
    public static final String FILTRO_INFORMATIVO_SEMANTICO_OK = "Semantic analysis informative successfully performed.";
    public static final String FILTRO_INFORMATIVO_SEMANTICO_FAIL = "Semantic analysis informative unsuccessfully performed.";


    public static final String PROBLEMA_CHECK_OUT = "There were problems during the check-out";
    public static final String PROBLEMA_COPY = "There were problems during the copy";

    public static final String DIRETORIOS_CONFLITO = "The following archives are physically broken";

    public static final String MENSAGEM_SINCRONIZACAO_TRUNK_COM_CONTEUDO_AUTOBRANCH = "Sincronizando trunk com o autobranch";
    public static final String MENSAGEM_SINCRONIZACAO_AUTOBRANCH_COM_CONTEUDO_TRUNK = "Sincronizando autobranch com o trunk";
    public static final String MENSAGEM_COMMIT_REINTEGRATE_TRUNK = "Sincronizando autobranch com o trunk";

    public static final String MENSAGEM_COMMIT_REINTEGRATE_TRUNK_FAIL = "check-in problem";

    public static final String INTEGRACAO_OK = "Integration successfully performed";
    public static final String INTEGRACAO_FAIL = "Integration unsuccessfully performed";


    private static String formatandoSaida(String saidaSemFormatação){
        return PROJETO_INICIO+saidaSemFormatação+PROJETO_FIM;
    }
    

}
