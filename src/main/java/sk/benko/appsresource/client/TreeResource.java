package sk.benko.appsresource.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree.Resources;

public interface TreeResource extends Resources {
  ImageResource treeOpen(); 
  ImageResource treeClosed();
  ImageResource treeLeaf();
}