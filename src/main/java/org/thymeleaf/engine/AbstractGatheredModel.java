/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.engine;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.IEngineContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.model.ICDATASection;
import org.thymeleaf.model.ICloseElementTag;
import org.thymeleaf.model.IComment;
import org.thymeleaf.model.IDocType;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IProcessingInstruction;
import org.thymeleaf.model.IStandaloneElementTag;
import org.thymeleaf.model.IText;
import org.thymeleaf.model.IXMLDeclaration;


/*
 *
 * @author Daniel Fernandez
 * @since 3.0.0
 *
 */
abstract class AbstractGatheredModel implements IGatheredModel {


    private final IEngineContext context;
    private final Model gatheredModel;

    private boolean gathered = false;

    private int modelLevel;


    AbstractGatheredModel(final IEngineConfiguration configuration, final IEngineContext context) {

        super();

        this.context = context;
        this.gatheredModel = new Model(configuration, context.getTemplateMode());

        this.gathered = false;

        this.modelLevel = 0;

        if (this.context == null) {
            throw new TemplateProcessingException(
                    "Neither iteration nor model gathering are supported because local variable support is DISABLED. " +
                    "This is due to the use of an implementation of the " + ITemplateContext.class.getName() + " interface " +
                    "that does not provide local-variable support. In order to have local-variable support, the context " +
                    "implementation should also implement the " + IEngineContext.class.getName() +
                    " interface");
        }


    }



    public final boolean isGathered() {
        return this.gathered;
    }


    protected final Model getGatheredModel() {
        return this.gatheredModel;
    }


    protected final IEngineContext getContext() {
        return this.context;
    }


    public final Model getInnerModel() {
        return this.gatheredModel;
    }



    public abstract void process(final ITemplateHandler handler);




    public final void gatherText(final IText text) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(text);
    }


    public final void gatherComment(final IComment comment) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(comment);
    }


    public final void gatherCDATASection(final ICDATASection cdataSection) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(cdataSection);
    }


    public final void gatherStandaloneElement(final IStandaloneElementTag standaloneElementTag) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(standaloneElementTag);
        if (this.modelLevel == 0) {
            this.gathered = true;
        }
    }


    public final void gatherOpenElement(final IOpenElementTag openElementTag) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(openElementTag);
        this.modelLevel++;
    }


    public final void gatherCloseElement(final ICloseElementTag closeElementTag) {
        if (closeElementTag.isUnmatched()) {
            gatherUnmatchedCloseElement(closeElementTag);
            return;
        }
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.modelLevel--;
        this.gatheredModel.add(closeElementTag);
        if (this.modelLevel == 0) {
            // OK, we are finished gathering, this close tag ends the process
            this.gathered = true;
        }
    }


    public final void gatherUnmatchedCloseElement(final ICloseElementTag closeElementTag) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(closeElementTag);
    }


    public final void gatherDocType(final IDocType docType) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(docType);
    }


    public final void gatherXMLDeclaration(final IXMLDeclaration xmlDeclaration) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(xmlDeclaration);
    }


    public final void gatherProcessingInstruction(final IProcessingInstruction processingInstruction) {
        if (this.gathered) {
            throw new TemplateProcessingException("Gathering is finished already! We cannot gather more events");
        }
        this.gatheredModel.add(processingInstruction);
    }

    
}