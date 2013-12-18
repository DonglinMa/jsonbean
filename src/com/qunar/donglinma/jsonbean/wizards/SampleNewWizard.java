package com.qunar.donglinma.jsonbean.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;

import java.io.*;

import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;

import com.qunar.donglinma.util.ToolEntry;

/**
 * This is a sample new wizard. Its role is to create a new file resource in the provided container. If the container
 * resource (a folder or a project) is selected in the workspace when the wizard is opened, it will accept it as the
 * target container. The wizard creates one file with the extension "mpe". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will be able to open it.
 */

public class SampleNewWizard extends Wizard implements INewWizard {
    private SampleNewWizardPage page;
    private ISelection selection;
    private ToolEntry entry = new ToolEntry();

    /**
     * Constructor for SampleNewWizard.
     */
    public SampleNewWizard() {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
     * Adding the page to the wizard.
     */

    public void addPages() {
        page = new SampleNewWizardPage(selection);
        addPage(page);
    }

    /**
     * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using
     * wizard as execution context.
     */
    public boolean performFinish() {
        final String path = page.getContainerName();
        final String className = page.getClassName();
        final String packageName = page.getPackageName();
        final String jsonContent = page.getJsonContent().replaceAll("\n", "");
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doFinish(path, className, packageName, jsonContent, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.toString());
            return false;
        }
        return true;
    }

    /**
     * The worker method. It will find the container, create the file if missing or just replace its contents, and open
     * the editor on the newly created file.
     */

    private void doFinish(
		String containerName,
		String className,
		String packageName,
		String jsonContent,
		IProgressMonitor monitor)
		throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + className , 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		
		Map<String, String> files = entry.generateJavaBean(className, packageName, jsonContent);
		for(Entry<String, String> specFile : files.entrySet()){
		    final IFile file = container.getFile(new Path(specFile.getKey()));
		    try {
	            InputStream stream = new ByteArrayInputStream(specFile.getValue().getBytes());
	            if (file.exists()) {
	                file.setContents(stream, true, true, monitor);
	            } else {
	                file.create(stream, true, monitor);
	            }
	            stream.close();
	        } catch (IOException e) {
	        }
		}
		
		final IFile currentFile = container.getFile(new Path(className + ".java"));
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
				    
					IDE.openEditor(page, currentFile, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, "com.qunar.donglinma.jsonbean", IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
}