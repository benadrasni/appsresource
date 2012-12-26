package sk.benko.appsresource.server;

import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.AppEngineUploadAction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import au.com.bytecode.opencsv.CSVReader;

public class ImportObjectsUploadAction extends AppEngineUploadAction { 
  private static final Logger log = Logger.getLogger(ImportObjectsUploadAction.class.getName());

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * A reference to the data store.
   */
  private final StoreDB store = new StoreDB();

  @Override
  public String executeAction(HttpServletRequest request,
      List<FileItem> sessionFiles) throws UploadActionException {
    final StoreDB.Api api = store.getApi();
    String header = null;
    String userId = request.getParameter("user");
    String tId = request.getParameter("template");
    String appId = request.getParameter("app");
    try {
      int rowCount = 0;
      for (FileItem csvItem : sessionFiles) {
        InputStreamReader isr = new InputStreamReader(csvItem.getInputStream(), "UTF-8");
        BufferedReader input =  new BufferedReader(isr);
        
        int headerId = api.saveImportHeader(userId, appId, tId, csvItem.getName());
        header = input.readLine();
        int columnCount = header.split(",").length;

        CSVReader reader = new CSVReader(input);
        List<String[]> myEntries = reader.readAll();
        
        for (String[] myEntry : myEntries)
          if (myEntry.length == columnCount)
            api.saveImportRow(headerId, rowCount++, myEntry);
      }
      if (rowCount == 0) {
        api.rollback();
        header = null;
      } else  
        api.commit();
      removeSessionFileItems(request);
    } catch (Exception ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      api.rollback();
      header = null;
    }
    return header;
  }
} 
