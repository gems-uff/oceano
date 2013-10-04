//selecionar tamanho da combobox
function expandSELECT(sel) {
    sel.style.width = '';
}

function contractSELECT(sel) {
    sel.style.width = '25em';
}



function selecionItemComboBox(valor, idSelect){
    var select = document.getElementById(idSelect);
    if(select==null) return;
    var op = select.options;
    var fim = op.length-1;
    for(i = fim; i>0; i--){      
        if (op[i].text == valor){
            select.selectedIndex = i;
            return;
        }
    }
}

var oldObj = "";
var oldValor = "";
var inteiro = new RegExp("[0-9]");

function formataData(campo, teclapres)
{
    
    var tecla = teclapres.keyCode;
    
    if (tecla == 8 || tecla == 46)
    {
        return; /* Quando BackSpace ou Delete for pressionado, deixa o usuario fazer o que quiser */
    }
        
    valorSemFormato = replaceAll(campo.value,"/","");
        
    tamanho = valorSemFormato.length;
    if(tamanho >=8) valorSemFormato = valorSemFormato.substring(0,7);
        
    if (tecla >= 48 && tecla <= 57 || tecla >= 96 && tecla <= 105 )
    {
                
        if ((tamanho >= 2) && (tamanho <= 3)){
            campo.value = valorSemFormato.substr(0, 2) + "/" + valorSemFormato.substr(2, tamanho);
        } else if ((tamanho >= 4) && (tamanho <= 9)){
            campo.value = valorSemFormato.substr(0, 2) + "/" + valorSemFormato.substr(2, 2) + "/" + valorSemFormato.substr(4, 4);
        } else{
            campo.value = valorSemFormato;
        }
        
        
    }
    
/*if()
    if(!isDataEnter(valorSemFormato)){
    campo.value = campo.value.substr(0,(campo.value.length -1));
    }
     */
    
} /* formatarData */

function isDataEnter(s){
    
    if (s.length == 0) return false;
    
    var caracteresValidos = "0123456789/";
    var character;
    
    for (i = 0; i <= s.length; i++)
    {
            
        character = s.charAt(i);
        if (caracteresValidos.indexOf(character) == -1)
        {
            return false;
        }
                
    }
            
    return true;
            
} 

var paginaAtual;
function pgAtual(){
    return paginaAtual;
}
function setPgAtual(pg){
    paginaAtual = pg;
}
/**
 * quando a tela estiver com links desabilitados, o elemento buscado deverá ser o LI
 */
function selAba(ind){
    var p = document.getElementById('barraCentral');
    fim = p.getElementsByTagName("li").length;
    for(i = 0; i<fim; i++){
        p.getElementsByTagName("li").item(i).style.backgroundColor = "#eeeee0";
        p.getElementsByTagName("li").item(i).style.border = "3px solid #fff";
        p.getElementsByTagName("li").item(i).style.color = "#000000";
    }
    p.getElementsByTagName("li").item(ind-1).style.backgroundColor ="#ccccff";
    p.getElementsByTagName("li").item(ind-1).style.border = "1px solid blue";
    
}

function selAbaEApareceHtml(ind){
    selAba(ind);
    var p = document.getElementById('telaCentralIncludes');
    fim = p.getElementsByTagName("dir").length;
    for(i = 0; i<fim; i++){
        p.getElementsByTagName("dir").item(i).style.display= "none";
    }
    p.getElementsByTagName("dir").item(ind-1).style.display ="";
    
}


function validaCnpj( campo ) {

    var numeros, digitos, soma, i, resultado, pos, tamanho, digitos_iguais, cnpj = campo.value.replace(/\D+/g, '');
    digitos_iguais = 1;
    if (cnpj.length != 14)
    {
        if(cnpj.length > 0 ) {
            alert('CNPJ inválido');
            campo.focus();
        }
        return false;
    }

    for (i = 0; i < cnpj.length - 1; i++)
        if (cnpj.charAt(i) != cnpj.charAt(i + 1))
        {
            digitos_iguais = 0;
            break;
        }
    if (!digitos_iguais)
    {
        tamanho = cnpj.length - 2
        numeros = cnpj.substring(0,tamanho);
        digitos = cnpj.substring(tamanho);
        soma = 0;
        pos = tamanho - 7;
        for (i = tamanho; i >= 1; i--)
        {
            soma += numeros.charAt(tamanho - i) * pos--;
            if (pos < 2)
                pos = 9;
        }
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(0))
        {
            alert('CNPJ inválido');
            c.focus();
            return false;
        }

        tamanho = tamanho + 1;
        numeros = cnpj.substring(0,tamanho);
        soma = 0;
        pos = tamanho - 7;
        for (i = tamanho; i >= 1; i--)
        {
            soma += numeros.charAt(tamanho - i) * pos--;
            if (pos < 2)
                pos = 9;
        }
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(1)){
            alert('CNPJ inválido');
            campo.focus();
            return false;
        }
        else {
            // alert('CNPJ  OK !');
            return true;
        }
    }
    else{
        alert('CNPJ inválido');
        campo.focus();
        return false;
    }
}


function CriaArray (n) {
    this.length = n
}
    
function dataEHoraCorrente(spanAlvo) {
    hoje = new Date()

    dia = hoje.getDate()
    dias = hoje.getDay()
    mes = hoje.getMonth()
    ano = hoje.getYear()
    ano = ano + 1900;
    if (dia < 10)
        dia = "0" + dia
    
    NomeDia = new CriaArray(7)
    NomeDia[0] = "Domingo"
    NomeDia[1] = "Segunda-feira"
    NomeDia[2] = "Terça-feira"
    NomeDia[3] = "Quarta-feira"
    NomeDia[4] = "Quinta-feira"
    NomeDia[5] = "Sexta-feira"
    NomeDia[6] = "Sábado"

    NomeMes = new CriaArray(12)
    NomeMes[0] = "Janeiro"
    NomeMes[1] = "Fevereiro"
    NomeMes[2] = "Março"
    NomeMes[3] = "Abril"
    NomeMes[4] = "Maio"
    NomeMes[5] = "Junho"
    NomeMes[6] = "Julho"
    NomeMes[7] = "Agosto"
    NomeMes[8] = "Setembro"
    NomeMes[9] = "Outubro"
    NomeMes[10] = "Novembro"
    NomeMes[11] = "Dezembro"

    spanAlvo.innerHTML = NomeDia[dias] + ", " + dia + " de " + NomeMes[mes] + " de " + ano;
}

