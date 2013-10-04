public aspect ErrosCompilacao{
    declare error: call(br.uff.ic.oceano.service..*.new(..)): "não é permitido INSTANCIAR um SERVICE sem ser pela Fábrica de Objetos";
    declare error: call(br.uff.ic.oceano.dao..*.new(..)): "Não é permitido INSTANCIAR um DAO sem ser pela Fábrica de Objetos";
    declare error: call(* br.uff.ic.oceano.core.factory.ObjectFactory.getObjectWithoutDataBaseDependencies(..)): "Utilize o getObjectWithoutDataBaseDependencies apenas fora do Oceano-core e Oceano-web";
}