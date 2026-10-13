package science.aist.msbpmn.service.transformation.impl.renderer;

import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.Expression;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.hl7.fhir.r4.model.TriggerDefinition;
import org.omg.spec.bpmn.model.ObjectFactory;
import org.omg.spec.bpmn.model.TConditionalEventDefinition;
import org.omg.spec.bpmn.model.TFormalExpression;
import org.springframework.stereotype.Component;
import science.aist.gtf.graph.Graph;
import science.aist.gtf.graph.Vertex;
import science.aist.msbpmn.service.transformation.renderer.AbstractBpmnEventDefinitionTransformationRenderer;

import javax.xml.bind.JAXBElement;
import java.util.function.Function;

/**
 * TODO: en este renderer hay que codificar lo necesario para la conversión de
 * action a callactivity
 * 
 * ahora mismo se ha copiado de
 * bpmnConditionalEventDefinitionTransformationRenderer, por eso está raro...
 * 
 * 
 * BpmnCallActivityTransformationRenderer
 */
@Component
public class BpmnCallActivityTransformationRenderer extends
        AbstractBpmnEventDefinitionTransformationRenderer<TConditionalEventDefinition, BackboneElement, PlanDefinition.PlanDefinitionActionComponent> {
    public BpmnCallActivityTransformationRenderer(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    protected Function<TConditionalEventDefinition, JAXBElement<? extends TConditionalEventDefinition>> constructJaxBElementMapping() {
        return getObjectFactory()::createConditionalEventDefinition;
    }

    @Override
    public TConditionalEventDefinition createElement() {
        return getObjectFactory().createTConditionalEventDefinition();
    }

    @Override
    public TConditionalEventDefinition mapProperties(TConditionalEventDefinition tConditionalEventDefinition,
            Graph<BackboneElement, Void> graph,
            Vertex<PlanDefinition.PlanDefinitionActionComponent, Void> currentElement) {
        tConditionalEventDefinition = super.mapProperties(tConditionalEventDefinition, graph, currentElement);

        TFormalExpression tExpression = getObjectFactory().createTFormalExpression();

        if (currentElement.getElement().hasTrigger()) {
            TriggerDefinition triggerFirstRep = currentElement.getElement().getTriggerFirstRep();
            if (triggerFirstRep.hasCondition()) {
                Expression expression = triggerFirstRep.getCondition();
                tExpression.setLanguage(expression.getLanguage());
                tExpression.getContent().add(expression.getExpression());
            }
        }

        tConditionalEventDefinition.setCondition(tExpression);

        return tConditionalEventDefinition;
    }
}
