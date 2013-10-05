package br.uff.ic.gems.peixeespadacliente.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import net.sf.refactorit.classmodel.BinSourceConstruct;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.classmodel.references.ProjectReference;
import net.sf.refactorit.common.util.AppRegistry;
import net.sf.refactorit.common.util.CFlowContext;
import net.sf.refactorit.commonIDE.DefaultWorkspaceManager;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.loader.ProjectLoader;
import net.sf.refactorit.query.usage.Finder;
import net.sf.refactorit.test.Utils;
import net.sf.refactorit.vfs.SourceMap;
import net.sf.refactorit.vfs.SourcesWithErrors;

/**
 *
 * @author Heliomar, João Felipe
 */
public class SetupUtils {

    private SetupUtils() {
    }

    public static void setup() {
        Utils.setUpTestingEnvironment();
    }

    public static void setup(Project p) {
        Utils.setUpTestingEnvironment();
        IDEController.getInstance().setActiveProject(p);
    }

    public static void restoreRefactoritStaticFields(boolean fullRestore) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException {
        // Todas as classes abaixo possuem static fields, mas não parece ser 
        // necessário resetar todos. Logo, só resetei os que considerei necessários 
        
        /* net.sf.refactorit.commonIDE.IDEController * */
        ReflectionUtils.setStaticField(IDEController.class, "instance", null);
        
        /* net.sf.refactorit.test.Utils */
        ReflectionUtils.setStaticField(Utils.class, "testFileDir", null);
        ReflectionUtils.setStaticField(Utils.class, "testProjects", null);
        ReflectionUtils.setStaticField(Utils.class, "fakeProject", null);
        ReflectionUtils.setStaticField(Utils.class, "initialized", false);
        ReflectionUtils.setStaticField(Utils.class, "lastSetOfFiles", new HashMap());
        
        if (fullRestore) {
            /* net.sf.refactorit.Version */
            /* net.sf.refactorit.audit.Audit */
            /* net.sf.refactorit.audit.ExplanationAction */
            /* net.sf.refactorit.audit.MultiTargetCorrectiveAction */
            /* net.sf.refactorit.audit.MultiTargetGroupingAction */

            /* net.sf.refactorit.audit.RuleViolation * */
    //        ReflectionUtils.setStaticField(
    //                RuleViolation.class, "usedTags", new MultiValueMap());

            /* net.sf.refactorit.audit.SkipTagHelper */
            /* net.sf.refactorit.audit.SkipUnskipAction */
            /* net.sf.refactorit.audit.pmd.InterfaceToPMD */
            /* net.sf.refactorit.audit.rules.BooleanLiteralComparisonRule */

            /* net.sf.refactorit.audit.rules.DangerousIteratorUsageRule */
    //        ReflectionUtils.setStaticField(
    //                DangerousIteratorUsageRule.class, "iterTypeRef", null);
    //        ReflectionUtils.setStaticField(
    //                DangerousIteratorUsageRule.class, "nextMethod", null);
    //        ReflectionUtils.setStaticField(
    //                DangerousIteratorUsageRule.class, "hasNextMethod", null);

            /* net.sf.refactorit.audit.rules.EmptyBlocksAndBodiesRule */
            /* net.sf.refactorit.audit.rules.EmptyStatementRule */
            /* net.sf.refactorit.audit.rules.EqualsHashcodeRule */
            /* net.sf.refactorit.audit.rules.EqualsOnDiffTypesRule */
            /* net.sf.refactorit.audit.rules.FloatEqualComparisionRule */
            /* net.sf.refactorit.audit.rules.IntDivFloatContextRule */
            /* net.sf.refactorit.audit.rules.LoopCondModificationRule */
            /* net.sf.refactorit.audit.rules.LoopConditionRule */
            /* net.sf.refactorit.audit.rules.MissingBlockRule */
            /* net.sf.refactorit.audit.rules.NestedBlockRule */
            /* net.sf.refactorit.audit.rules.NonStaticReferenceRule */
            /* net.sf.refactorit.audit.rules.NotUsedRulesAddOn */
            /* net.sf.refactorit.audit.rules.NullParametersRule */
            /* net.sf.refactorit.audit.rules.PMDrulesAddOn */
            /* net.sf.refactorit.audit.rules.ParameterAssignmentRule */
            /* net.sf.refactorit.audit.rules.ParameterOrderRule */
            /* net.sf.refactorit.audit.rules.PossibleCallNPERule */
            /* net.sf.refactorit.audit.rules.PossibleLostOverrideRule */
            /* net.sf.refactorit.audit.rules.RedundantCastRule */
            /* net.sf.refactorit.audit.rules.RedundantInstanceofRule */

            /* net.sf.refactorit.audit.rules.RemoveBracketsAction */
    //        ReflectionUtils.setFinalStaticField(
    //                RemoveBracketsAction.class, "INSTANCE", new RemoveBracketsAction());

            /* net.sf.refactorit.audit.rules.RemoveRedundantCast * */
    //        ReflectionUtils.setFinalStaticField(
    //                RemoveRedundantCast.class, "instance", new RemoveRedundantCast());

            /* net.sf.refactorit.audit.rules.SelfAssignmentRule */
            /* net.sf.refactorit.audit.rules.ShadingRule */
            /* net.sf.refactorit.audit.rules.StaticFieldAccessorsRule */
            /* net.sf.refactorit.audit.rules.StringConcatOrderRule */
            /* net.sf.refactorit.audit.rules.StringEqualComparisionRule */
            /* net.sf.refactorit.audit.rules.StringEqualsOrderRule */
            /* net.sf.refactorit.audit.rules.StringToStringRule */
            /* net.sf.refactorit.audit.rules.SwitchCaseFallthroughRule */
            /* net.sf.refactorit.audit.rules.SwitchMissingDefaultRule */
            /* net.sf.refactorit.audit.rules.UnusedAssignmentRule */
            /* net.sf.refactorit.audit.rules.UnusedImportRule */
            /* net.sf.refactorit.audit.rules.UnusedLocalVariableRule */

            /* net.sf.refactorit.audit.rules.UnusedTagsRule * */
    //        ReflectionUtils.setFinalStaticField(
    //                UnusedTagsRule.class, "instance", new UnusedTagsRule());

            /* net.sf.refactorit.audit.rules.complexity.LawOfDemeterRule */
            /* net.sf.refactorit.audit.rules.complexity.MethodBodyLengthRule */
            /* net.sf.refactorit.audit.rules.complexity.MethodCallsMethodRule */
            /* net.sf.refactorit.audit.rules.exceptions.AbortedFinallyRule */
            /* net.sf.refactorit.audit.rules.exceptions.DangerousCatchRule */
            /* net.sf.refactorit.audit.rules.exceptions.DangerousThrowRule */
            /* net.sf.refactorit.audit.rules.exceptions.RedundantThrowsRule */
            /* net.sf.refactorit.audit.rules.inheritance.AbstractOverrideRule */
            /* net.sf.refactorit.audit.rules.inheritance.AbstractSubclassRule */
            /* net.sf.refactorit.audit.rules.inheritance.HiddenFieldRule */
            /* net.sf.refactorit.audit.rules.inheritance.HiddenStaticMethodRule */

            /* net.sf.refactorit.audit.rules.j2se5.ForinForArrCorrectiveAction */
    //        ReflectionUtils.setFinalStaticField(
    //                ForinForArrCorrectiveAction.class, "instance", new ForinForArrCorrectiveAction());

            /* net.sf.refactorit.audit.rules.j2se5.ForinForIteratorCorrectiveAction */
    //        ReflectionUtils.setFinalStaticField(
    //                ForinForIteratorCorrectiveAction.class, "instance", new ForinForIteratorCorrectiveAction());

            /* net.sf.refactorit.audit.rules.j2se5.ForinRule */

            /* net.sf.refactorit.audit.rules.j2se5.ForinWhileIteratorCorrectiveAction */
    //        ReflectionUtils.setFinalStaticField(
    //                ForinWhileIteratorCorrectiveAction.class, "instance", new ForinWhileIteratorCorrectiveAction());

            /* net.sf.refactorit.audit.rules.j2se5.GenericsArgumentsAnalyzer */
            /* net.sf.refactorit.audit.rules.j2se5.GenericsRule */
            /* net.sf.refactorit.audit.rules.j2se5.RedundantBoxingRule */
            /* net.sf.refactorit.audit.rules.j2se5.RedundantUnboxingRule */
            /* net.sf.refactorit.audit.rules.misc.DebugCodeRule */
            /* net.sf.refactorit.audit.rules.misc.EarlyDeclarationRule */

            /* net.sf.refactorit.audit.rules.misc.numericliterals.ManageNumericLiteralsAction */
    //        ReflectionUtils.setFinalStaticField(
    //                ManageNumericLiteralsAction.class, "instance", new ManageNumericLiteralsAction());

            /* net.sf.refactorit.audit.rules.misc.numericliterals.NumericLiteralsRule */
            /* net.sf.refactorit.audit.rules.modifiers.ConstantFieldProposalRule */
            /* net.sf.refactorit.audit.rules.modifiers.FinalLocalProposalRule */
            /* net.sf.refactorit.audit.rules.modifiers.FinalMethodProposalRule */
            /* net.sf.refactorit.audit.rules.modifiers.MemberUsageCollector */
            /* net.sf.refactorit.audit.rules.modifiers.MinimizeAccessRule */
            /* net.sf.refactorit.audit.rules.modifiers.ModifierOrderRule */
            /* net.sf.refactorit.audit.rules.modifiers.PseudoAbstractClassRule */
            /* net.sf.refactorit.audit.rules.modifiers.RedundantModifiersRule */
            /* net.sf.refactorit.audit.rules.modifiers.SingleAssignmentFinalRule */
            /* net.sf.refactorit.audit.rules.modifiers.StaticMethodProposalRule */
            /* net.sf.refactorit.audit.rules.performance.ForLoopConditionOptimizer */
            /* net.sf.refactorit.audit.rules.serialization.NotSerializableSuperRule */
            /* net.sf.refactorit.audit.rules.serialization.SerialVersionUIDRule */
            /* net.sf.refactorit.audit.rules.service.ServiceAnnotationUsagesRule */
            /* net.sf.refactorit.audit.rules.service.ServiceBinItemReferenceRule */
            /* net.sf.refactorit.audit.rules.service.ServiceEnumUsagesRule */
            /* net.sf.refactorit.audit.rules.service.ServiceForinUsagesRule */
            /* net.sf.refactorit.audit.rules.service.ServiceGenericsUsagesRule */
            /* net.sf.refactorit.classfile.ClassData */

            /* net.sf.refactorit.classmodel.BinConstructor */
    //        ReflectionUtils.setFinalStaticField(
    //                BinConstructor.class, "NO_CONSTRUCTORS", new BinConstructor[0]);

            /* net.sf.refactorit.classmodel.BinExpressionList */
    //        ReflectionUtils.setFinalStaticField(
    //                BinExpressionList.class, "NO_EXPRESSIONLIST", new BinExpressionList(new BinExpression[0]));

            /* net.sf.refactorit.classmodel.BinInitializer */
    //        ReflectionUtils.setFinalStaticField(
    //                BinInitializer.class, "NO_INITIALIZERS", new BinInitializer[0]);

            /* net.sf.refactorit.classmodel.BinMethod * */
    //        ReflectionUtils.setFinalStaticField(
    //                BinMethod.class, "NO_METHODS", new BinMethod[0]);


            /* net.sf.refactorit.classmodel.BinPackage */

            /* net.sf.refactorit.classmodel.BinParameter * */
    //        ReflectionUtils.setFinalStaticField(
    //                BinParameter.class, "NO_PARAMS", new BinParameter[0]);

            /* net.sf.refactorit.classmodel.BinPrimitiveType */

            /* net.sf.refactorit.classmodel.BinSourceConstruct * */
            ReflectionUtils.setFinalStaticField(
                    BinSourceConstruct.class, "compoundsCache", new HashMap(1024));

            /* net.sf.refactorit.classmodel.Project */
            /* net.sf.refactorit.classmodel.TypeConversionRules */
            /* net.sf.refactorit.classmodel.references.BinItemReference */
            /* net.sf.refactorit.classmodel.references.BinItemReference */

            /* net.sf.refactorit.classmodel.references.ProjectReference */
            ReflectionUtils.setFinalStaticField(
                    ProjectReference.class, "referencePool", new HashMap());

            /* net.sf.refactorit.classmodel.statements.BinFieldDeclaration */
    //        ReflectionUtils.setFinalStaticField(
    //                BinFieldDeclaration.class, "NO_FIELDDECLARATIONS", new BinFieldDeclaration[0]);

            /* net.sf.refactorit.classmodel.statements.BinStatement */
    //        ReflectionUtils.setFinalStaticField(
    //                BinStatement.class, "NO_STATEMENTS", new BinStatement[0]);

            /* net.sf.refactorit.cli.Arguments */
            /* net.sf.refactorit.cli.ArgumentsValidator */
            /* net.sf.refactorit.cli.SupportedArguments */
            /* net.sf.refactorit.cli.UsageInfo */

            /* net.sf.refactorit.common.util.AdaptiveMultiValueMap */
    //        ReflectionUtils.setFinalStaticField(
    //                AdaptiveMultiValueMap.class, "EMPTY_MAP", new AdaptiveMultiValueMap(0));

            /* net.sf.refactorit.common.util.AppRegistry */
            ReflectionUtils.setStaticField(
                    AppRegistry.class, "instance", 
                    ReflectionUtils.instantiatePrivate(AppRegistry.class));

            /* net.sf.refactorit.common.util.Assert */
            /* net.sf.refactorit.common.util.Base64 */

            /* net.sf.refactorit.common.util.CFlowContext */
            ReflectionUtils.setFinalStaticField(
                    CFlowContext.class, "objects", new Hashtable());

            /* net.sf.refactorit.common.util.CollectionUtil */
    //        ReflectionUtils.setFinalStaticField(
    //                CollectionUtil.class, "EMPTY_SET", 
    //                ReflectionUtils.instantiatePrivate(CollectionUtil.EMPTY_SET.getClass()));
    //        ReflectionUtils.setFinalStaticField(
    //                CollectionUtil.class, "EMPTY_MAP", new HashMap(0));
    //        ReflectionUtils.setFinalStaticField(
    //                CollectionUtil.class, "EMPTY_ARRAY_LIST", new ArrayList(0) {
    //
    //            public void add(int i1, Object obj2) {
    //                throw new UnsupportedOperationException("this is unmodifiable list");
    //            }
    //
    //            public boolean add(Object obj) {
    //                throw new UnsupportedOperationException("this is unmodifiable list");
    //            }
    //
    //            public Object set(int i1, Object obj2) {
    //                throw new UnsupportedOperationException("this is unmodifiable list");
    //            }
    //
    //            public boolean addAll(int i1, Collection collection2) {
    //                throw new UnsupportedOperationException("this is unmodifiable list");
    //            }
    //
    //            public boolean addAll(Collection collection) {
    //                throw new UnsupportedOperationException("this is unmodifiable list");
    //            }
    //        });

            /* net.sf.refactorit.common.util.FileExtensionFilter */

            /* net.sf.refactorit.common.util.FileReadWriteUtil * */
    //        ReflectionUtils.setFinalStaticField(
    //                FileReadWriteUtil.class, "lockedFiles", new Vector());

            /* net.sf.refactorit.common.util.HexUtil */
            /* net.sf.refactorit.common.util.HtmlUtil */
            /* net.sf.refactorit.common.util.StringUtil */
            /* net.sf.refactorit.common.util.WordUtils */
            /* net.sf.refactorit.common.util.ZipUtil */

            /* net.sf.refactorit.common.util.graph.Digraph */
    //        ReflectionUtils.setFinalStaticField(
    //                Digraph.class, "emptyList", new ArrayList(0));

            /* net.sf.refactorit.common.util.graph.WeightedGraph */
            /* net.sf.refactorit.common.util.png.Chunk */

            /* net.sf.refactorit.commonIDE.DefaultWorkspaceManager */
            ReflectionUtils.setStaticField(
                    DefaultWorkspaceManager.class, "manager", 
                    ReflectionUtils.instantiatePrivate(DefaultWorkspaceManager.class));

            /* net.sf.refactorit.commonIDE.MenuBuilder */
            /* net.sf.refactorit.commonIDE.options.ClassPathEditingPanel */
            /* net.sf.refactorit.commonIDE.options.JvmSelector */
            /* net.sf.refactorit.commonIDE.options.Path */
            /* net.sf.refactorit.ejb.RitEjbConstants */
            /* net.sf.refactorit.exception.ErrorCodes */
            /* net.sf.refactorit.jsp.JspUtil */
            /* net.sf.refactorit.loader.ASTTreeCache */
            /* net.sf.refactorit.loader.JavadocComment */
            /* net.sf.refactorit.loader.LoadingASTUtil */

            /* net.sf.refactorit.loader.ProjectLoader */
            ReflectionUtils.setFinalStaticField(
                    ProjectLoader.class, "createdItems", new ArrayList());

            /* net.sf.refactorit.loader.RebuildLogic */
            /* net.sf.refactorit.loader.Settings */
            /* net.sf.refactorit.memory.BaseMemoryArea */
            /* net.sf.refactorit.memory.MemoryTrack */

            /* net.sf.refactorit.memory.SourcesMemoryMap */
    //        ReflectionUtils.setStaticField(
    //                SourcesMemoryMap.class, "mappings", null);

            /* net.sf.refactorit.metrics.MetricsAction */
            /* net.sf.refactorit.metrics.MetricsModel */
            /* net.sf.refactorit.options.GlobalOptions */
            /* net.sf.refactorit.parser.ASTCompressor */
            /* net.sf.refactorit.parser.ASTImplFactory */
    //        ReflectionUtils.setFinalStaticField(
    //                ASTImplFactory.class, "INSTANCE", new ASTImplFactory());

            /* net.sf.refactorit.parser.FastJavaLexer */
            /* net.sf.refactorit.parser.OptimizedJavaRecognizer */
            /* net.sf.refactorit.query.AbstractIndexer */
            /* net.sf.refactorit.query.dependency.GLGraphTraverser */
            /* net.sf.refactorit.query.dependency.GraphPanel */
            /* net.sf.refactorit.query.notused.ExcludeFilterRule */
            /* net.sf.refactorit.query.notused.NotUsedIndexer */
            /* net.sf.refactorit.query.notused.NotUsedTreeTableModel */
            /* net.sf.refactorit.query.structure.AbstractSearch */
            /* net.sf.refactorit.query.structure.FieldSearch */
            /* net.sf.refactorit.query.structure.FindRequest */
            /* net.sf.refactorit.query.structure.ParamSearch */
            /* net.sf.refactorit.query.text.QualifiedNameIndexer */
            /* net.sf.refactorit.query.usage.ConstructorIndexer */

            /* net.sf.refactorit.query.usage.Finder * */
            ReflectionUtils.setFinalStaticField(
                    Finder.class, "invocationMap", new HashMap());

            /* net.sf.refactorit.refactorings.ConflictsTreeNode */
            /* net.sf.refactorit.refactorings.EjbUtil */
            /* net.sf.refactorit.refactorings.RefactoringStatus */
            /* net.sf.refactorit.refactorings.changesignature.ChangeMethodSignatureRefactoring */
            /* net.sf.refactorit.refactorings.changesignature.analyzer.CallNode */
            /* net.sf.refactorit.refactorings.changesignature.MethodInvocationError */
            /* net.sf.refactorit.refactorings.cleanimports.CleanImportsRefactoring */
            /* net.sf.refactorit.refactorings.apisnapshot.Snapshot */
            /* net.sf.refactorit.refactorings.apisnapshot.SnapshotBuilder */
            /* net.sf.refactorit.refactorings.apisnapshot.SnapshotIO */
            /* net.sf.refactorit.refactorings.apisnapshot.SnapshotItem */
            /* net.sf.refactorit.refactorings.conflicts.AddImplementationConflict */
            /* net.sf.refactorit.refactorings.conflicts.ChangedFunctionalityConflict */
            /* net.sf.refactorit.refactorings.conflicts.ConflictType */
            /* net.sf.refactorit.refactorings.conflicts.CreateOnlyDeclarationConflict */
            /* net.sf.refactorit.refactorings.conflicts.DeclarationOrDefinitionConlfict */
            /* net.sf.refactorit.refactorings.conflicts.DeleteOtherImplementersConflict */
            /* net.sf.refactorit.refactorings.conflicts.ImportNotPossibleConflict */
            /* net.sf.refactorit.refactorings.conflicts.InstanceNotAccessibleConflict */
            /* net.sf.refactorit.refactorings.conflicts.MakeStaticConflict */
            /* net.sf.refactorit.refactorings.conflicts.MRUsedByConflict */
            /* net.sf.refactorit.refactorings.conflicts.MRUsesConflict */
            /* net.sf.refactorit.refactorings.conflicts.OtherImplementersExistConflict */
            /* net.sf.refactorit.refactorings.createconstructor.CreateConstructor */
            /* net.sf.refactorit.refactorings.createmissing.CreateMissingMethodRefactoring */
            /* net.sf.refactorit.refactorings.delegate.AddDelegatesRefactoring */
            /* net.sf.refactorit.refactorings.delegate.OverrideMethodsRefactoring */
            /* net.sf.refactorit.refactorings.encapsulatefield.EncapsulateEditor */
            /* net.sf.refactorit.refactorings.encapsulatefield.EncapsulateField */
            /* net.sf.refactorit.refactorings.encapsulatefield.EncapsulateFields */
            /* net.sf.refactorit.refactorings.extract.ExtractMethod */
            /* net.sf.refactorit.refactorings.extract.ExtractMethodAnalyzer */
            /* net.sf.refactorit.refactorings.extract.FlowAnalyzer */
            /* net.sf.refactorit.refactorings.extract.ReturnThrowAnalyzer */
            /* net.sf.refactorit.refactorings.extract.VariableUseAnalyzer */
            /* net.sf.refactorit.refactorings.extractsuper.ExtractSuper */
            /* net.sf.refactorit.refactorings.factorymethod.FactoryMethod */
            /* net.sf.refactorit.refactorings.inlinemethod.InlineMethod */
            /* net.sf.refactorit.refactorings.inlinevariable.InlineVariable */
            /* net.sf.refactorit.refactorings.introducetemp.IntroduceTemp */
            /* net.sf.refactorit.refactorings.minaccess.MinimizeAccess */
            /* net.sf.refactorit.refactorings.minaccess.MinimizeAccessUtil */
            /* net.sf.refactorit.refactorings.movemember.MoveMember */
            /* net.sf.refactorit.refactorings.movemember.MoveMemberModel */
            /* net.sf.refactorit.refactorings.movetype.MoveType */
            /* net.sf.refactorit.refactorings.promotetemptofield.PromoteTempToField */
            /* net.sf.refactorit.refactorings.promotetemptofield.ui.UserInput */
            /* net.sf.refactorit.refactorings.pullpush.PullPush */
            /* net.sf.refactorit.refactorings.rename.RenameField */
            /* net.sf.refactorit.refactorings.rename.RenameLabel */
            /* net.sf.refactorit.refactorings.rename.RenameLocal */
            /* net.sf.refactorit.refactorings.rename.RenameMethod */
            /* net.sf.refactorit.refactorings.rename.RenamePackage */
            /* net.sf.refactorit.refactorings.rename.RenameRefactoring */
            /* net.sf.refactorit.refactorings.rename.RenameType */
            /* net.sf.refactorit.refactorings.undo.BackupManagerUtil */
            /* net.sf.refactorit.refactorings.undo.BackupRepository */
            /* net.sf.refactorit.refactorings.undo.CreateFileUndo */
            /* net.sf.refactorit.refactorings.undo.MilestoneManager * */
            /* net.sf.refactorit.refactorings.undo.RenameFileUndo */
            /* net.sf.refactorit.refactorings.undo.RitUndoManager */
            /* net.sf.refactorit.refactorings.undo.UndoableStatus */
            /* net.sf.refactorit.refactorings.usesupertype.TypeUsage */
            /* net.sf.refactorit.refactorings.usesupertype.UsageInfoCollector */
            /* net.sf.refactorit.refactorings.usesupertype.UseSuperTypeRefactoring */
            /* net.sf.refactorit.reports.Statistics */
            /* net.sf.refactorit.source.UserFriendlyError */
            /* net.sf.refactorit.source.edit.DirCreator */
            /* net.sf.refactorit.source.edit.EditorManager */
            /* net.sf.refactorit.source.edit.FileCreator */
            /* net.sf.refactorit.source.edit.FileEraser */
            /* net.sf.refactorit.source.edit.FileRenamer */
            /* net.sf.refactorit.source.edit.Line */
            /* net.sf.refactorit.source.format.FormatSettings */
            /* net.sf.refactorit.source.html.HTMLASTIndexer */
            /* net.sf.refactorit.source.html.HTMLLinkIndexer */
            /* net.sf.refactorit.source.preview.Diff */
            /* net.sf.refactorit.standalone.JBrowserPanel */
            /* net.sf.refactorit.standalone.JRefactorItDialog */
            /* net.sf.refactorit.standalone.JRefactorItFrame */
            /* net.sf.refactorit.standalone.JRowsHeader */
            /* net.sf.refactorit.standalone.JStartupDialog */
            /* net.sf.refactorit.standalone.RefactorItProject */


            /* net.sf.refactorit.test.commonIDE.MockWorkspace */

            /* net.sf.refactorit.test.commonIDE.MockWorkspaceManager */
    //        ReflectionUtils.setStaticField(
    //                MockWorkspaceManager.class, "manager", 
    //                ReflectionUtils.instantiatePrivate(MockWorkspaceManager.class));

            /* net.sf.refactorit.test.commonIDE.NullWorkspace */

            /* net.sf.refactorit.test.commonIDE.NullWorkspaceManager */
    //        ReflectionUtils.setStaticField(
    //                NullWorkspaceManager.class, "manager", 
    //                ReflectionUtils.instantiatePrivate(NullWorkspaceManager.class));        

            /* net.sf.refactorit.transformations.DeleteTransformation */
            /* net.sf.refactorit.transformations.RenamePackageTransformation */
            /* net.sf.refactorit.transformations.TransformationManager */

            //TODO: buscar static em net.sf.refactorit.ui

            /* net.sf.refactorit.utils.AuditTablePopupUtils */
            /* net.sf.refactorit.utils.CommentOutHelper */
            /* net.sf.refactorit.utils.FileUtil */
            /* net.sf.refactorit.utils.LinePositionUtil */
            /* net.sf.refactorit.utils.NumericLiteralsUtils */
            /* net.sf.refactorit.utils.RefactorItConstants */
            /* net.sf.refactorit.utils.cvsutil.CvsFileStatus */
            /* net.sf.refactorit.vfs.AbstractClassPath */

            /* net.sf.refactorit.vfs.SourceMap */
            ReflectionUtils.setFinalStaticField(
                    SourceMap.class, "sources", new HashMap());

            /* net.sf.refactorit.vfs.SourcesWithErrors * */
            ReflectionUtils.setStaticField(
                    SourcesWithErrors.class, "instance", null);

            /* net.sf.refactorit.vfs.local.LocalSource */
            /* net.sf.refactorit.vfs.local.LocalSourcePath */
            /* net.sf.refactorit.vfs.local.TestLocalFS */
            /* net.sf.refactorit.vfs.local.ZipEntrySource */
            /* net.sf.refactorit.vfs.local.ZipSource */

            //TODO: buscar static em test
        }
    }
}
