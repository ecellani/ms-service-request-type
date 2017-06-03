<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cus="http://www.gvt.com.br/CustomerManagement/CustomerSelfManagement/CustomerSelfEmpowered/CustomerSelfEmpowered/" xmlns:sear="http://www.gvt.com.br/CustomerManagement/CustomerSelfManagement/CustomerSelfEmpowered/CustomerSelfEmpowered/searchServiceRequestTypeEntities">
   <soapenv:Header/>
   <soapenv:Body>
      <cus:searchServiceRequestTypeIn>
         <sear:serviceRequestType>
            <sear:channel>{data(/sear/channel)}</sear:channel>
            <sear:serviceId>{data(/sear/serviceId)}</sear:serviceId>
         </sear:serviceRequestType>
      </cus:searchServiceRequestTypeIn>
   </soapenv:Body>
</soapenv:Envelope>
