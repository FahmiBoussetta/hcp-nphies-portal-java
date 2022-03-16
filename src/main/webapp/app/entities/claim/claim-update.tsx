import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, FormGroup, Label, Modal, ModalBody, Form, Input } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedBlobField, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IEncounter } from 'app/shared/model/encounter.model';
import { getEntities as getEncounters } from 'app/entities/encounter/encounter.reducer';
import { ICoverageEligibilityResponse } from 'app/shared/model/coverage-eligibility-response.model';
import { getEntities as getCoverageEligibilityResponses } from 'app/entities/coverage-eligibility-response/coverage-eligibility-response.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IOrganization } from 'app/shared/model/organization.model';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { IReferenceIdentifier } from 'app/shared/model/reference-identifier.model';
import { getEntities as getReferenceIdentifiers } from 'app/entities/reference-identifier/reference-identifier.reducer';
import { IPayee } from 'app/shared/model/payee.model';
import { getEntities as getPayees } from 'app/entities/payee/payee.reducer';
import { ILocation } from 'app/shared/model/location.model';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { IAccident } from 'app/shared/model/accident.model';
import { getEntities as getAccidents } from 'app/entities/accident/accident.reducer';
import { getEntities as getClaims } from 'app/entities/claim/claim.reducer';
import { getEntities as getPractitioners } from 'app/entities/practitioner/practitioner.reducer';
import { getEntities as getPractitionerRoles } from 'app/entities/practitioner-role/practitioner-role.reducer';
import { getEntities as getInsurances } from 'app/entities/insurance/insurance.reducer';
import { getEntities as getCoverages } from 'app/entities/coverage/coverage.reducer';
import { getEntities as getClaimResponses } from 'app/entities/claim-response/claim-response.reducer';
import { getEntities as getCareTeams } from 'app/entities/care-team/care-team.reducer';
import { getEntities as getSupportingInfos } from 'app/entities/supporting-info/supporting-info.reducer';
import { getEntities as getRelateds } from 'app/entities/related/related.reducer';
import { getEntities as getInformationSequences } from 'app/entities/information-sequence/information-sequence.reducer';
import { getEntities as getItems } from 'app/entities/item/item.reducer';
import { getEntities as getDetailItems } from 'app/entities/detail-item/detail-item.reducer';
import { getEntities as getSubDetailItems } from 'app/entities/sub-detail-item/sub-detail-item.reducer';
import { getEntity, updateEntity, createEntity, reset } from './claim.reducer';
import { IClaim } from 'app/shared/model/claim.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {
  getClaimTerm,
  getCovTerm,
  getCrTerm,
  getLocTerm,
  getOrgTerm,
  getPatTerm,
  getPractitionerRoleTerm,
  getPractitionerTerm,
  getProdTerm,
  matchClaimToTerm,
  matchCovToTerm,
  matchCrToTerm,
  matchLocToTerm,
  matchOrgToTerm,
  matchPatToTerm,
  matchPractitionerRoleToTerm,
  matchPractitionerToTerm,
  matchProdToTerm,
} from 'app/shared/util/autocomplete-utils';
import Autocomplete from 'react-autocomplete';
import {
  getCareTeamTerm,
  getDiagnosisTerm,
  getInsurancesTerm,
  getRelatedTerm,
  getSupportingInfoTerm,
  getUdiTerm,
  initialCareTeamFormData,
  initialDetailItemFormData,
  initialDetailUdiFormData,
  initialDiagnosisFormData,
  initialInsurancesFormData,
  initialRelatedFormData,
  initialSubDetailItemFormData,
  initialSubDetailUdiFormData,
  initialSupportingInfoFormData,
  initialUdiFormData,
} from 'app/shared/util/formdata-utils';
import { getItemTerm, initialItemFormData } from 'app/shared/util/formdata-utils';
import { ClaimRelationshipEnum } from 'app/shared/model/enumerations/claim-relationship-enum.model';
import { getProducts } from '../item/item.reducer';
import _ from 'lodash';

const MyModal = ({ children, isOpen, toggle }) => (
  <div>
    <Modal isOpen={isOpen} toggle={toggle} style={{ maxWidth: '1600px', width: '70%', margin: '10px auto' }}>
      <ModalBody>{children}</ModalBody>
    </Modal>
  </div>
);

export const ClaimUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const encounters = useAppSelector(state => state.encounter.entities);
  const coverageEligibilityResponses = useAppSelector(state => state.coverageEligibilityResponse.entities);
  const patients = useAppSelector(state => state.patient.entities);
  const insurances = useAppSelector(state => state.insurance.entities);
  const coverages = useAppSelector(state => state.coverage.entities);
  const claimResponses = useAppSelector(state => state.claimResponse.entities);
  const careTeams = useAppSelector(state => state.careTeam.entities);
  const supportingInfos = useAppSelector(state => state.supportingInfo.entities);
  const relateds = useAppSelector(state => state.related.entities);
  const informationSequences = useAppSelector(state => state.informationSequence.entities);
  const items = useAppSelector(state => state.item.entities);
  const detailItems = useAppSelector(state => state.detailItem.entities);
  const subDetailItems = useAppSelector(state => state.subDetailItem.entities);
  const organizations = useAppSelector(state => state.organization.entities);
  const practitioners = useAppSelector(state => state.practitioner.entities);
  const practitionerRoles = useAppSelector(state => state.practitionerRole.entities);
  const referenceIdentifiers = useAppSelector(state => state.referenceIdentifier.entities);
  const payees = useAppSelector(state => state.payee.entities);
  const locations = useAppSelector(state => state.location.entities);
  const accidents = useAppSelector(state => state.accident.entities);
  const claims = useAppSelector(state => state.claim.entities);
  const claimEntity = useAppSelector(state => state.claim.entity);
  const loading = useAppSelector(state => state.claim.loading);
  const updating = useAppSelector(state => state.claim.updating);
  const updateSuccess = useAppSelector(state => state.claim.updateSuccess);
  const products = useAppSelector(state => state.item.links);

  const [patientId, setPatientId] = useState('');
  const [patient, setPatient] = useState({});

  useEffect(() => {
    if (!isNew && claimEntity.patient && patientId === '' && claimEntity.id?.toString() === props.match.params.id) {
      setPatient(claimEntity.patient);
      setPatientId(getPatTerm(claimEntity.patient));
    }
  }, [claimEntity.patient]);

  const changePatient = e => {
    setPatientId(e.target.value);
  };

  const selectPatient = (val, item) => {
    setPatient(item);
    setPatientId(val);
  };

  const [provId, setProvId] = useState('');
  const [provider, setProv] = useState({});

  useEffect(() => {
    if (!isNew && claimEntity.provider && provId === '' && claimEntity.id?.toString() === props.match.params.id) {
      setProv(claimEntity.provider);
      setProvId(getOrgTerm(claimEntity.provider));
    }
  }, [claimEntity.provider]);

  const changeProv = e => {
    setProvId(e.target.value);
  };

  const selectProv = (val, item) => {
    setProv(item);
    setProvId(val);
  };

  const [insId, setInsId] = useState('');
  const [insurer, setIns] = useState({});

  useEffect(() => {
    if (!isNew && claimEntity.insurer && insId === '' && claimEntity.id?.toString() === props.match.params.id) {
      setIns(claimEntity.insurer);
      setInsId(getOrgTerm(claimEntity.insurer));
    }
  }, [claimEntity.insurer]);

  const changeIns = e => {
    setInsId(e.target.value);
  };

  const selectIns = (val, item) => {
    setIns(item);
    setInsId(val);
  };

  const [locId, setLocId] = useState('');
  const [facility, setLoc] = useState({});

  useEffect(() => {
    if (!isNew && claimEntity.facility && locId === '' && claimEntity.id?.toString() === props.match.params.id) {
      setLoc(claimEntity.facility);
      setLocId(getLocTerm(claimEntity.facility));
    }
  }, [claimEntity.facility]);

  const changeLoc = e => {
    setLocId(e.target.value);
  };

  const selectLoc = (val, item) => {
    setLoc(item);
    setLocId(val);
  };

  useEffect(() => {
    if (!isNew && claimEntity.prescription && claimEntity.id?.toString() === props.match.params.id) {
      setHasPrescription(true);
    }
  }, [claimEntity.prescription]);

  const [hasPrescription, setHasPrescription] = useState(false);

  const changePrescription = e => {
    setHasPrescription(e.target.value === '1');
  };

  useEffect(() => {
    if (!isNew && claimEntity.originalPrescription && claimEntity.id?.toString() === props.match.params.id) {
      setHasOriginalPrescription(true);
    }
  }, [claimEntity.originalPrescription]);

  const [hasOriginalPrescription, setHasOriginalPrescription] = useState(false);

  const changeOriginalPrescription = e => {
    setHasOriginalPrescription(e.target.value === '1');
  };

  useEffect(() => {
    if (!isNew && claimEntity.referral && claimEntity.id?.toString() === props.match.params.id) {
      setHasReferral(true);
    }
  }, [claimEntity.referral]);

  const [hasReferral, setHasReferral] = useState(false);

  const changeReferral = e => {
    setHasReferral(e.target.value === '1');
  };

  useEffect(() => {
    if (!isNew && claimEntity.accident && claimEntity.id?.toString() === props.match.params.id) {
      setHasAccident(true);
    }
  }, [claimEntity.accident]);

  const [hasAccident, setHasAccident] = useState(false);

  const changeAccident = e => {
    setHasAccident(e.target.value === '1');
  };

  const [partyPatientId, setPartyPatientId] = useState('');
  const [partyPatient, setPartyPatient] = useState({});

  const changePartyPatient = e => {
    setPartyPatientId(e.target.value);
  };

  const selectPartyPatient = (val, item) => {
    setPartyPatient(item);
    setPartyPatientId(val);
  };

  const [partyOrganizationId, setPartyOrganizationId] = useState('');
  const [partyOrganization, setPartyOrganization] = useState({});

  const changePartyOrganization = e => {
    setPartyOrganizationId(e.target.value);
  };

  const selectPartyOrganization = (val, item) => {
    setPartyOrganization(item);
    setPartyOrganizationId(val);
  };

  useEffect(() => {
    if (!isNew && payees.length > 0 && claimEntity.payee && claimEntity.id?.toString() === props.match.params.id) {
      if (claimEntity.payee?.type === 'Subscriber') {
        setPartyType('patient');
      } else if (claimEntity.payee?.type === 'Provider') {
        setPartyType('organization');
      } else if (partyOrganizationId === '') {
        const payee = payees.length > 0 ? payees.filter(e => e.id?.toString() === claimEntity.payee.id?.toString())[0] : null;
        setPartyType('none');
        setPartyOrganization(payee.partyOrganization);
        setPartyOrganizationId(getOrgTerm(payee.partyOrganization));
      }
    }
  }, [claimEntity.payee, payees]);

  const [partyType, setPartyType] = useState('none');

  const changePartyType = e => {
    setPartyType(e.target.value);
  };

  const [isRequired, setRequired] = useState([]);

  const [itemFormData, updateItemFormData] = React.useState(initialItemFormData);
  const [itemList, setItemList] = useState([]);
  const [itemDiagList, setItemDiagList] = useState([]);
  const [itemSuppList, setItemSuppList] = useState([]);
  const [isOpenItem, setIsOpenItem] = useState(false);
  const toggleItem = () => setIsOpenItem(!isOpenItem);

  useEffect(() => {
    if (items.length > 0 && claimEntity.items && itemList.length === 0 && claimEntity.id?.toString() === props.match.params.id) {
      const list = [];
      claimEntity.items.forEach(element => {
        if (element !== null) {
          const el = items.find(e => e.id === element.id);
          const detList = [];
          detailItems
            .filter(e => e.item?.id?.toString() === el.id?.toString())
            .forEach(det => {
              const subDetList = [];
              subDetailItems
                .filter(e => e.detailItem?.id?.toString() === det.id?.toString())
                .forEach(subDet => {
                  subDetList.push({
                    ...subDet,
                    udis: referenceIdentifiers.filter(e => e.subDetailItem?.id?.toString() === subDet.id?.toString()),
                  });
                });
              detList.push({
                ...det,
                subDetails: subDetList,
                udis: referenceIdentifiers.filter(e => e.detailItem?.id?.toString() === det.id?.toString()),
              });
            });
          list.push({
            ...el,
            details: detList,
          });
        }
      });
      setItemList(list);
    }
  }, [claimEntity.items, items]);

  const handleItemDiagChange = e => {
    const l = [...itemDiagList];
    const i = parseInt(e.target.name.replace('diag', ''), 10);
    if (e.target.checked) {
      l.push({ diagSeq: i });
      l.sort(function (a, b) {
        return a.diagSeq - b.diagSeq;
      });
      setItemDiagList(l);
    } else {
      setItemDiagList(l.filter(item => item.diagSeq !== i));
    }
  };

  const handleItemSuppChange = e => {
    const l = [...itemSuppList];
    const i = parseInt(e.target.name.replace('supp', ''), 10);
    if (e.target.checked) {
      l.push({ infSeq: i });
      l.sort(function (a, b) {
        return a.infSeq - b.infSeq;
      });
      setItemSuppList(l);
    } else {
      setItemSuppList(l.filter(item => item.infSeq !== i));
    }
  };

  const handleItemChange = e => {
    let v = e.target.name === 'isPackage' ? e.target.checked : e.target.value.trim();
    if (e.target.name === 'careTeamSequences') {
      v = [{ careSeq: e.target.value }];
    }
    updateItemFormData({
      ...itemFormData,
      [e.target.name.indexOf('sequence') > -1 ? 'sequence' : e.target.name]: v,
    });
  };

  const handleItemSubmit = e => {
    e.preventDefault();
    const required = [];
    setRequired([]);
    addItem();
    toggleItem();
    updateItemFormData(initialItemFormData);
  };

  const addItem = () => {
    const newItemList = [...itemList];
    const itemEntity = { ...itemFormData };
    itemEntity.sequence = itemList.length + 1;
    if (itemDate) {
      itemEntity.servicedDate = convertDateTimeToServer(itemEntity.servicedDate);
      delete itemEntity.servicedDateStart;
      delete itemEntity.servicedDateEnd;
    } else {
      itemEntity.servicedDateStart = convertDateTimeToServer(itemEntity.servicedDateStart);
      itemEntity.servicedDateEnd = convertDateTimeToServer(itemEntity.servicedDateEnd);
      delete itemEntity.servicedDate;
    }
    const s = product.substring(product.indexOf('.txt|') + 5);
    switch (productType) {
      case 'Imaging':
        itemEntity.imaging = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Laboratory':
        itemEntity.laboratory = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'MedicalDevice':
        itemEntity.medicalDevice = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'MedicationCodes':
        itemEntity.medicationCode = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'OralHealthIP':
        itemEntity.oralHealthIP = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'OralHealthOP':
        itemEntity.oralHealthOP = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Procedure':
        itemEntity.procedure = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Services':
        itemEntity.services = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'TransportationSRCA':
        itemEntity.transportationSRCA = s.substring(0, s.indexOf('|')).trim();
        break;
      default:
        break;
    }
    if (detailItemList.length > 0 && withDetail) {
      itemEntity.details = detailItemList;
    }
    if (itemDiagList.length > 0) {
      itemEntity.diagnosisSequences = itemDiagList;
    }
    if (itemSuppList.length > 0) {
      itemEntity.informationSequences = itemSuppList;
    }
    newItemList.push(itemEntity);
    setItemList(newItemList);
  };

  const removeItem = () => {
    const newItemList = [...itemList];
    newItemList.pop();
    setItemList(newItemList);
  };

  const [productId, setProductId] = useState('');
  const [product, setProduct] = useState('');
  const [filterProducts, setFilterProducts] = useState([]);
  const [productType, setProductType] = useState('');
  const [itemDate, setItemDate] = useState(true);

  const changeItemDate = e => {
    setItemDate(e.target.value === '0');
  };

  const changeProductType = e => {
    setProductType(e.target.value);
    const filter = [];
    products.forEach(element => {
      if (element.startsWith(e.target.value)) {
        filter.push(element);
      }
    });
    setFilterProducts(filter);
  };

  const changeProduct = e => {
    setProductId(e.target.value);
  };

  const selectProduct = (val, item) => {
    setProduct(item);
    setProductId(val);
  };

  const [withDetail, setWithDetail] = useState(false);
  const [productDetailId, setProductDetailId] = useState('');
  const [productDetail, setProductDetail] = useState('');
  const [filterDetailProducts, setFilterDetailProducts] = useState([]);
  const [productDetailType, setProductDetailType] = useState('');

  const changeWithDetail = e => {
    setWithDetail(e.target.value === '1');
  };

  const [detailItemFormData, updateDetailItemFormData] = React.useState(initialDetailItemFormData);
  const [detailItemList, setDetailItemList] = useState([]);

  const handleDetailItemChange = e => {
    updateDetailItemFormData({
      ...detailItemFormData,
      [e.target.name.indexOf('sequence') > -1 ? 'sequence' : e.target.name]: e.target.value.trim(),
    });
  };

  const handleDetailItemSubmit = e => {
    e.preventDefault();
    const required = [];
    setRequired([]);
    addDetailItem();
    updateDetailItemFormData(initialItemFormData);
  };

  const addDetailItem = () => {
    const newDetailItemList = [...detailItemList];
    const itemEntity = { ...detailItemFormData };
    itemEntity.sequence = detailItemList.length + 1;
    const s = productDetail.substring(productDetail.indexOf('.txt|') + 5);
    switch (productDetailType) {
      case 'Imaging':
        itemEntity.imaging = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Laboratory':
        itemEntity.laboratory = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'MedicalDevice':
        itemEntity.medicalDevice = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'MedicationCodes':
        itemEntity.medicationCode = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'OralHealthIP':
        itemEntity.oralHealthIP = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'OralHealthOP':
        itemEntity.oralHealthOP = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Procedure':
        itemEntity.procedure = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Services':
        itemEntity.services = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'TransportationSRCA':
        itemEntity.transportationSRCA = s.substring(0, s.indexOf('|')).trim();
        break;
      default:
        break;
    }
    if (detailUdiList.length > 0 && withDetailUdi) {
      itemEntity.udis = detailUdiList;
    }
    if (subDetailItemList.length > 0 && withSubDetail) {
      itemEntity.subDetails = subDetailItemList;
    }
    newDetailItemList.push(itemEntity);
    setDetailItemList(newDetailItemList);
  };

  const removeDetailItem = () => {
    const newDetailItemList = [...detailItemList];
    newDetailItemList.pop();
    setDetailItemList(newDetailItemList);
  };

  const changeProductDetailType = e => {
    setProductDetailType(e.target.value);
    const filter = [];
    products.forEach(element => {
      if (element.startsWith(e.target.value)) {
        filter.push(element);
      }
    });
    setFilterDetailProducts(filter);
  };

  const changeProductDetail = e => {
    setProductDetailId(e.target.value);
  };

  const selectProductDetail = (val, item) => {
    setProductDetail(item);
    setProductDetailId(val);
  };

  const [withSubDetail, setWithSubDetail] = useState(false);
  const [productSubDetailId, setProductSubDetailId] = useState('');
  const [productSubDetail, setProductSubDetail] = useState('');
  const [filterSubDetailProducts, setFilterSubDetailProducts] = useState([]);
  const [productSubDetailType, setProductSubDetailType] = useState('');

  const changeWithSubDetail = e => {
    setWithSubDetail(e.target.value === '1');
  };

  const [subDetailItemFormData, updateSubDetailItemFormData] = React.useState(initialSubDetailItemFormData);
  const [subDetailItemList, setSubDetailItemList] = useState([]);

  const handleSubDetailItemChange = e => {
    updateSubDetailItemFormData({
      ...subDetailItemFormData,
      [e.target.name.indexOf('sequence') > -1 ? 'sequence' : e.target.name]: e.target.value.trim(),
    });
  };

  const handleSubDetailItemSubmit = e => {
    e.preventDefault();
    const required = [];
    setRequired([]);
    addSubDetailItem();
    updateSubDetailItemFormData(initialItemFormData);
  };

  const addSubDetailItem = () => {
    const newSubDetailItemList = [...subDetailItemList];
    const itemEntity = { ...subDetailItemFormData };
    itemEntity.sequence = subDetailItemList.length + 1;
    const s = productSubDetail.substring(productSubDetail.indexOf('.txt|') + 5);
    switch (productSubDetailType) {
      case 'Imaging':
        itemEntity.imaging = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Laboratory':
        itemEntity.laboratory = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'MedicalDevice':
        itemEntity.medicalDevice = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'MedicationCodes':
        itemEntity.medicationCode = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'OralHealthIP':
        itemEntity.oralHealthIP = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'OralHealthOP':
        itemEntity.oralHealthOP = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Procedure':
        itemEntity.procedure = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'Services':
        itemEntity.services = s.substring(0, s.indexOf('|')).trim();
        break;
      case 'TransportationSRCA':
        itemEntity.transportationSRCA = s.substring(0, s.indexOf('|')).trim();
        break;
      default:
        break;
    }
    if (subDetailUdiList.length > 0 && withSubDetailUdi) {
      itemEntity.udis = subDetailUdiList;
    }
    newSubDetailItemList.push(itemEntity);
    setSubDetailItemList(newSubDetailItemList);
  };

  const removeSubDetailItem = () => {
    const newSubDetailItemList = [...subDetailItemList];
    newSubDetailItemList.pop();
    setSubDetailItemList(newSubDetailItemList);
  };

  const changeProductSubDetailType = e => {
    setProductSubDetailType(e.target.value);
    const filter = [];
    products.forEach(element => {
      if (element.startsWith(e.target.value)) {
        filter.push(element);
      }
    });
    setFilterSubDetailProducts(filter);
  };

  const changeProductSubDetail = e => {
    setProductSubDetailId(e.target.value);
  };

  const selectProductSubDetail = (val, item) => {
    setProductSubDetail(item);
    setProductSubDetailId(val);
  };

  const [udiFormData, updateUdiFormData] = React.useState(initialUdiFormData);
  const [udiList, setUdiList] = useState([]);

  const handleUdiChange = e => {
    updateUdiFormData({
      ...udiFormData,
      [e.target.name]: e.target.value.trim(),
    });
  };

  const handleUdiSubmit = e => {
    e.preventDefault();
    const required = [];
    if (!udiFormData.ref && !udiFormData.identifier) {
      required.push('udi');
    } else {
      setRequired([]);
      addUdi();
      updateUdiFormData(initialUdiFormData);
    }
  };

  const addUdi = () => {
    const newUdiList = [...udiList];
    const udiEntity = { ...udiFormData };
    if (!withUdiIdentifier) {
      udiEntity.ref = 'Device';
    }
    newUdiList.push(udiEntity);
    setUdiList(newUdiList);
  };

  const removeUdi = index => {
    const newUdiList = [...udiList];
    newUdiList.splice(index, 1);
    setUdiList(newUdiList);
  };

  const [withUdi, setWithUdi] = useState(false);

  const changeWithUdi = e => {
    setWithUdi(e.target.value === '1');
  };

  const [withUdiIdentifier, setWithUdiIdentifier] = useState(false);

  const changeWithUdiIdentifier = e => {
    setWithUdiIdentifier(e.target.value === '1');
  };

  const [detailUdiFormData, updateDetailUdiFormData] = React.useState(initialDetailUdiFormData);
  const [detailUdiList, setDetailUdiList] = useState([]);

  const handleDetailUdiChange = e => {
    updateDetailUdiFormData({
      ...detailUdiFormData,
      [e.target.name]: e.target.value.trim(),
    });
  };

  const handleDetailUdiSubmit = e => {
    e.preventDefault();
    const required = [];
    if (!detailUdiFormData.ref && !detailUdiFormData.identifier) {
      required.push('detailUdi');
    } else {
      setRequired([]);
      addDetailUdi();
      updateDetailUdiFormData(initialDetailUdiFormData);
    }
  };

  const addDetailUdi = () => {
    const newDetailUdiList = [...detailUdiList];
    const detailUdiEntity = { ...detailUdiFormData };
    if (!withDetailUdiIdentifier) {
      detailUdiEntity.ref = 'Device';
    }
    newDetailUdiList.push(detailUdiEntity);
    setDetailUdiList(newDetailUdiList);
  };

  const removeDetailUdi = index => {
    const newDetailUdiList = [...detailUdiList];
    newDetailUdiList.splice(index, 1);
    setDetailUdiList(newDetailUdiList);
  };

  const [withDetailUdi, setWithDetailUdi] = useState(false);

  const changeWithDetailUdi = e => {
    setWithDetailUdi(e.target.value === '1');
  };

  const [withDetailUdiIdentifier, setWithDetailUdiIdentifier] = useState(false);

  const changeWithDetailUdiIdentifier = e => {
    setWithDetailUdiIdentifier(e.target.value === '1');
  };

  const [subDetailUdiFormData, updateSubDetailUdiFormData] = React.useState(initialSubDetailUdiFormData);
  const [subDetailUdiList, setSubDetailUdiList] = useState([]);

  const handleSubDetailUdiChange = e => {
    updateSubDetailUdiFormData({
      ...subDetailUdiFormData,
      [e.target.name]: e.target.value.trim(),
    });
  };

  const handleSubDetailUdiSubmit = e => {
    e.preventDefault();
    const required = [];
    if (!subDetailUdiFormData.ref && !subDetailUdiFormData.identifier) {
      required.push('subDetailUdi');
    } else {
      setRequired([]);
      addSubDetailUdi();
      updateSubDetailUdiFormData(initialSubDetailUdiFormData);
    }
  };

  const addSubDetailUdi = () => {
    const newSubDetailUdiList = [...subDetailUdiList];
    const subDetailUdiEntity = { ...subDetailUdiFormData };
    if (!withSubDetailUdiIdentifier) {
      subDetailUdiEntity.ref = 'Device';
    }
    newSubDetailUdiList.push(subDetailUdiEntity);
    setSubDetailUdiList(newSubDetailUdiList);
  };

  const removeSubDetailUdi = index => {
    const newSubDetailUdiList = [...subDetailUdiList];
    newSubDetailUdiList.splice(index, 1);
    setSubDetailUdiList(newSubDetailUdiList);
  };

  const [withSubDetailUdi, setWithSubDetailUdi] = useState(false);

  const changeWithSubDetailUdi = e => {
    setWithSubDetailUdi(e.target.value === '1');
  };

  const [withSubDetailUdiIdentifier, setWithSubDetailUdiIdentifier] = useState(false);

  const changeWithSubDetailUdiIdentifier = e => {
    setWithSubDetailUdiIdentifier(e.target.value === '1');
  };

  const [relatedFormData, updateRelatedFormData] = React.useState(initialRelatedFormData);
  const [relatedList, setRelatedList] = useState([]);
  const [isOpenRelated, setIsOpenRelated] = useState(false);
  const toggleRelated = () => setIsOpenRelated(!isOpenRelated);

  useEffect(() => {
    if (relateds.length > 0 && claimEntity.relateds && relatedList.length === 0 && claimEntity.id?.toString() === props.match.params.id) {
      const list = [];
      claimEntity.relateds.forEach(element => {
        if (element !== null) {
          list.push(relateds.find(e => e.id === element.id));
        }
      });
      setRelatedList(list);
    }
  }, [claimEntity.relateds, relateds]);

  const handleRelatedChange = e => {
    if (e.target.name === 'relationShip') {
      updateRelatedFormData({
        ...relatedFormData,
        [e.target.name]: e.target.value.trim(),
      });
    } else {
      const newRelated = {
        ...relatedFormData,
        claimReference: { ref: 'Claim', identifier: e.target.value.trim() },
      };
      updateRelatedFormData(newRelated);
    }
  };

  const handleRelatedSubmit = e => {
    e.preventDefault();
    const required = [];
    if (!relatedFormData.relationShip || !relatedFormData.claimReference?.identifier) {
      required.push('related');
    } else {
      setRequired([]);
      addRelated();
      toggleRelated();
      updateRelatedFormData(initialRelatedFormData);
      setRelatedClaimId('');
    }
  };

  const addRelated = () => {
    const newRelatedList = [...relatedList];
    const relatedEntity = { ...relatedFormData };
    newRelatedList.push(relatedEntity);
    setRelatedList(newRelatedList);
  };

  const removeRelated = index => {
    const newRelatedList = [...relatedList];
    newRelatedList.splice(index, 1);
    setRelatedList(newRelatedList);
  };

  const [withClaimIdentifier, setWithClaimIdentifier] = useState(false);

  const changeWithClaim = e => {
    setWithClaimIdentifier(e.target.value === '1');
  };

  const [relatedClaimId, setRelatedClaimId] = useState('');
  const [relatedClaim, setRelatedClaim] = useState({});

  const changeRelatedClaim = e => {
    setRelatedClaimId(e.target.value);
  };

  const selectRelatedClaim = (val, item) => {
    setRelatedClaim(item);
    setRelatedClaimId(val);
    const newRelated = {
      ...relatedFormData,
      claimReference: { ref: 'Claim', identifier: item.identifier },
    };
    updateRelatedFormData(newRelated);
  };

  const [careTeamFormData, updateCareTeamFormData] = React.useState(initialCareTeamFormData);
  const [careTeamList, setCareTeamList] = useState([]);
  const [isOpenCareTeam, setIsOpenCareTeam] = useState(false);
  const toggleCareTeam = () => setIsOpenCareTeam(!isOpenCareTeam);

  useEffect(() => {
    if (
      careTeams.length > 0 &&
      claimEntity.careTeams &&
      careTeamList.length === 0 &&
      claimEntity.id?.toString() === props.match.params.id
    ) {
      const list = [];
      claimEntity.careTeams.forEach(element => {
        if (element !== null) {
          list.push(careTeams.find(e => e.id === element.id));
        }
      });
      setCareTeamList(list);
    }
  }, [claimEntity.careTeams, careTeams]);

  const handleCareTeamChange = e => {
    updateCareTeamFormData({
      ...careTeamFormData,
      [e.target.name.indexOf('sequence') > -1 ? 'sequence' : e.target.name]: e.target.value.trim(),
    });
  };

  const handleCareTeamSubmit = e => {
    e.preventDefault();
    const required = [];
    setRequired([]);
    addCareTeam();
    toggleCareTeam();
    updateCareTeamFormData(initialCareTeamFormData);
    setCareTeamPractitionerId('');
    setCareTeamPractitionerRoleId('');
  };

  const addCareTeam = () => {
    const newCareTeamList = [...careTeamList];
    const careTeamEntity = { ...careTeamFormData };
    careTeamEntity.sequence = careTeamList.length + 1;
    if (withoutRole) {
      careTeamEntity.provider = careTeamPractitioner;
    } else {
      careTeamEntity.providerRole = careTeamPractitionerRole;
    }
    newCareTeamList.push(careTeamEntity);
    setCareTeamList(newCareTeamList);
  };

  const removeCareTeam = () => {
    const newCareTeamList = [...careTeamList];
    newCareTeamList.pop();
    setCareTeamList(newCareTeamList);
  };

  const [withoutRole, setWithoutRole] = useState(true);

  const changeWithoutRole = e => {
    setWithoutRole(e.target.value === '1');
  };

  const [careTeamPractitionerId, setCareTeamPractitionerId] = useState('');
  const [careTeamPractitioner, setCareTeamPractitioner] = useState({});

  const changeCareTeamPractitioner = e => {
    setCareTeamPractitionerId(e.target.value);
  };

  const selectCareTeamPractitioner = (val, item) => {
    setCareTeamPractitioner(item);
    setCareTeamPractitionerId(val);
  };

  const [careTeamPractitionerRoleId, setCareTeamPractitionerRoleId] = useState('');
  const [careTeamPractitionerRole, setCareTeamPractitionerRole] = useState({});

  const changeCareTeamPractitionerRole = e => {
    setCareTeamPractitionerRoleId(e.target.value);
  };

  const selectCareTeamPractitionerRole = (val, item) => {
    setCareTeamPractitionerRole(item);
    setCareTeamPractitionerRoleId(val);
  };

  const [insurancesFormData, updateInsurancesFormData] = React.useState(initialInsurancesFormData);
  const [insurancesList, setInsurancesList] = useState([]);
  const [isOpenInsurances, setIsOpenInsurances] = useState(false);
  const toggleInsurances = () => setIsOpenInsurances(!isOpenInsurances);

  useEffect(() => {
    if (
      insurances.length < 0 &&
      claimEntity.insurances &&
      insurancesList.length === 0 &&
      claimEntity.id?.toString() === props.match.params.id
    ) {
      const list = [];
      claimEntity.insurances.forEach(element => {
        if (element !== null) {
          list.push(insurances.find(e => e?.id === element.id));
        }
      });
      setInsurancesList(list);
    }
  }, [claimEntity.insurances, insurances]);

  const handleInsurancesChange = e => {
    const v = e.target.name === 'focal' ? e.target.checked : e.target.value.trim();
    updateInsurancesFormData({
      ...insurancesFormData,
      [e.target.name]: v,
    });
  };

  const handleInsurancesSubmit = e => {
    e.preventDefault();
    const required = [];
    setRequired([]);
    addInsurances();
    toggleInsurances();
    updateInsurancesFormData(initialInsurancesFormData);
  };

  const addInsurances = () => {
    const newInsurancesList = [...insurancesList];
    const insurancesEntity = { ...insurancesFormData };
    insurancesEntity.sequence = insurancesList.length + 1;
    insurancesEntity.coverage = coverage;
    if (crId !== '') {
      insurancesEntity.claimResponse = cr;
    }
    insurancesEntity.coverage = coverage;
    newInsurancesList.push(insurancesEntity);
    setInsurancesList(newInsurancesList);
  };

  const removeInsurances = () => {
    const newInsurancesList = [...insurancesList];
    newInsurancesList.pop();
    setInsurancesList(newInsurancesList);
  };

  const [coverageId, setCoverageId] = useState('');
  const [coverage, setCoverage] = useState({});

  const changeCoverage = e => {
    setCoverageId(e.target.value);
  };

  const selectCoverage = (val, item) => {
    setCoverage(item);
    setCoverageId(val);
  };

  const [crId, setCrId] = useState('');
  const [cr, setCr] = useState({});

  const changeCr = e => {
    setCrId(e.target.value);
  };

  const selectCr = (val, item) => {
    setCr(item);
    setCrId(val);
  };

  const [diagnosisFormData, updateDiagnosisFormData] = React.useState(initialDiagnosisFormData);
  const [diagnosisList, setDiagnosisList] = useState([]);
  const [isOpenDiagnosis, setIsOpenDiagnosis] = useState(false);
  const toggleDiagnosis = () => setIsOpenDiagnosis(!isOpenDiagnosis);

  useEffect(() => {
    if (claimEntity.diagnoses && diagnosisList.length === 0 && claimEntity.id?.toString() === props.match.params.id) {
      const list = [];
      claimEntity.diagnoses.forEach(element => {
        if (element !== null) {
          list.push({
            ...element,
          });
        }
      });
      setDiagnosisList(list);
    }
  }, [claimEntity.diagnoses]);

  const handleDiagnosisChange = e => {
    updateDiagnosisFormData({
      ...diagnosisFormData,
      [e.target.name.indexOf('sequence') > -1 ? 'sequence' : e.target.name]: e.target.value.trim(),
    });
  };

  const handleDiagnosisSubmit = e => {
    e.preventDefault();
    const required = [];
    setRequired([]);
    addDiagnosis();
    toggleDiagnosis();
    updateDiagnosisFormData(initialDiagnosisFormData);
  };

  const addDiagnosis = () => {
    const newDiagnosisList = [...diagnosisList];
    const diagnosisEntity = { ...diagnosisFormData };
    diagnosisEntity.sequence = diagnosisList.length + 1;
    const d = diagnosis.substring(diagnosis.indexOf('.txt|') + 5);
    diagnosisEntity.diagnosis = d.substring(0, d.indexOf('|')).trim();
    newDiagnosisList.push(diagnosisEntity);
    setDiagnosisList(newDiagnosisList);
  };

  const removeDiagnosis = () => {
    const newDiagnosisList = [...diagnosisList];
    newDiagnosisList.pop();
    setDiagnosisList(newDiagnosisList);
  };

  const [diagnosisId, setDiagnosisId] = useState('');
  const [diagnosis, setDiagnosis] = useState('');
  const [diagnoses, setDiagnoses] = useState([]);

  const changeDiagnosis = e => {
    setDiagnosisId(e.target.value);
  };

  const selectDiagnosis = (val, item) => {
    setDiagnosis(item);
    setDiagnosisId(val);
  };

  const [supportingInfoFormData, updateSupportingInfoFormData] = React.useState(initialSupportingInfoFormData);
  const [supportingInfoList, setSupportingInfoList] = useState([]);
  const [isOpenSupportingInfo, setIsOpenSupportingInfo] = useState(false);
  const toggleSupportingInfo = () => setIsOpenSupportingInfo(!isOpenSupportingInfo);

  useEffect(() => {
    if (
      supportingInfos.length > 0 &&
      claimEntity.supportingInfos &&
      supportingInfoList.length === 0 &&
      claimEntity.id?.toString() === props.match.params.id
    ) {
      const list = [];
      claimEntity.supportingInfos.forEach(element => {
        if (element !== null) {
          list.push(supportingInfos.find(e => e.id === element.id));
        }
      });
      setSupportingInfoList(list);
    }
  }, [claimEntity.supportingInfos, supportingInfos]);

  const handleSupportingInfoChange = async e => {
    if (e.target.value === '') {
      const formInf = _.omit({ ...supportingInfoFormData }, [e.target.name]);
      updateSupportingInfoFormData({
        ...formInf,
      });
    } else {
      if (e.target.name.indexOf('.') > -1) {
        const v = e.target.name.split('.');
        const newV = { ...supportingInfoFormData[v[0]], [v[1]]: e.target.files ? await readFileDataAsBase64(e) : e.target.value.trim() };
        updateSupportingInfoFormData({
          ...supportingInfoFormData,
          [v[0]]: newV,
        });
      } else {
        updateSupportingInfoFormData({
          ...supportingInfoFormData,
          [e.target.name.indexOf('sequence') > -1 ? 'sequence' : e.target.name]: e.target.value.trim(),
        });
      }
      if (e.target.name === 'category') {
        setIsMissingTooth(false);
        switch (e.target.value.trim()) {
          case 'Info':
            setInfoCodeType(-1);
            setInfoValueType(0);
            setTiming(-1);
            setUnit('');
            break;
          case 'Onset':
            setInfoCodeType(3);
            setInfoValueType(-1);
            setTiming(1);
            setUnit('');
            break;
          case 'Attachment':
            setInfoCodeType(-1);
            setInfoValueType(3);
            setTiming(-1);
            setUnit('');
            break;
          case 'Missingtooth':
            setInfoCodeType(1);
            setInfoValueType(-1);
            setTiming(1);
            setUnit('');
            setIsMissingTooth(true);
            break;
          case 'Hospitalized':
            setInfoCodeType(-1);
            setInfoValueType(-1);
            setTiming(2);
            setUnit('');
            break;
          case 'EmploymentImpacted':
            setInfoCodeType(-1);
            setInfoValueType(-1);
            setTiming(2);
            setUnit('');
            break;
          case 'Patient_Reason_for_Visit':
            setInfoCodeType(0);
            setInfoValueType(-1);
            setTiming(1);
            setUnit('');
            break;
          case 'Lab_test':
            setInfoCodeType(2);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('-1');
            break;
          case 'Reason_for_Visit':
            setInfoCodeType(0);
            setInfoValueType(-1);
            setTiming(1);
            setUnit('');
            break;
          case 'Days_Supply':
            setInfoCodeType(-1);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('d');
            break;
          case 'Vital_Sign_Weight':
            setInfoCodeType(-1);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('kg');
            break;
          case 'Vital_Sign_Systolic':
            setInfoCodeType(-1);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('mm[Hg]');
            break;
          case 'Vital_Sign_Diastolic':
            setInfoCodeType(-1);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('mm[Hg]');
            break;
          case 'Icu_hours':
            setInfoCodeType(-1);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('h');
            break;
          case 'Ventilation_hours':
            setInfoCodeType(-1);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('h');
            break;
          case 'Vital_Sign_Height':
            setInfoCodeType(-1);
            setInfoValueType(2);
            setTiming(-1);
            setUnit('cm');
            break;
          case 'Chief_Complaint':
            setInfoCodeType(3);
            setInfoValueType(-1);
            setTiming(-1);
            setUnit('');
            break;
          default:
            setInfoCodeType(-1);
            setInfoValueType(-1);
            setTiming(-1);
            setUnit('');
            break;
        }
      }
    }
  };

  const handleSupportingInfoSubmit = e => {
    e.preventDefault();
    setRequired([]);
    addSupportingInfo();
    toggleSupportingInfo();
    updateSupportingInfoFormData(initialSupportingInfoFormData);
  };

  const changeUnit = e => {
    setUnit(e.target.value);
  };

  const [isMissingTooth, setIsMissingTooth] = useState(false);
  const [withData, setWithData] = useState(true);

  const changeWithData = e => {
    setWithData(e.target.value === '1');
  };

  const readFileDataAsBase64 = e => {
    const file = e.target.files[0];

    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = event => {
        resolve(event.target.result);
      };

      reader.onerror = err => {
        reject(err);
      };

      reader.readAsDataURL(file);
    });
  };

  const [infoCodeType, setInfoCodeType] = useState(-1);
  const [infoValueType, setInfoValueType] = useState(-1);
  const [timing, setTiming] = useState(0);
  const [unit, setUnit] = useState('');

  const addSupportingInfo = () => {
    const newSupportingInfoList = [...supportingInfoList];
    const supportingInfoEntity = { ...supportingInfoFormData };
    supportingInfoEntity.sequence = supportingInfoList.length + 1;
    if (infoCodeType !== 0) {
      delete supportingInfoEntity.codeVisit;
    }
    if (infoCodeType !== 1) {
      delete supportingInfoEntity.codeFdiOral;
    }
    if (infoCodeType !== 2) {
      delete supportingInfoEntity.codeLOINC;
    }
    if (infoCodeType !== 3) {
      delete supportingInfoEntity.codeIcd;
    }
    if (infoCodeType !== 1) {
      delete supportingInfoEntity.codeFdiOral;
    }
    if (infoCodeType === 2) {
      supportingInfoEntity.codeLOINC = loinc;
    }
    if (infoCodeType === 3) {
      supportingInfoEntity.codeLOINC = icd;
    }
    if (infoValueType !== 0) {
      delete supportingInfoEntity.valueString;
    }
    if (infoValueType !== 1) {
      delete supportingInfoEntity.valueBoolean;
    }
    if (infoValueType !== 2) {
      delete supportingInfoEntity.valueQuantity;
    }
    if (infoValueType !== 3) {
      delete supportingInfoEntity.valueAttachment;
    }
    if (infoValueType !== 4) {
      delete supportingInfoEntity.valueReference;
    }
    if (infoValueType === 3) {
      supportingInfoEntity.valueAttachment.isData = withData;
    }
    if (timing > 0) {
      supportingInfoEntity.timing = convertDateTimeToServer(supportingInfoEntity.timing).toString();
      if (timing > 1) {
        supportingInfoEntity.timingEnd = convertDateTimeToServer(supportingInfoEntity.timingEnd).toString();
      }
    }
    newSupportingInfoList.push(supportingInfoEntity);
    setSupportingInfoList(newSupportingInfoList);
  };

  const removeSupportingInfo = () => {
    const newSupportingInfoList = [...supportingInfoList];
    newSupportingInfoList.pop();
    setSupportingInfoList(newSupportingInfoList);
  };

  const changeInfoCodeType = e => {
    setInfoCodeType(e.target.value);
  };

  const changeInfoValueType = e => {
    setInfoValueType(e.target.value);
  };

  const [loincId, setLoincId] = useState('');
  const [loinc, setLoinc] = useState('');
  const [loincs, setLoincs] = useState([]);

  const changeLoinc = e => {
    setLoincId(e.target.value);
  };

  const selectLoinc = (val, item) => {
    setLoinc(item);
    setLoincId(val);
  };

  const [icdId, setIcdId] = useState('');
  const [icd, setIcd] = useState('');

  const changeIcd = e => {
    setIcdId(e.target.value);
  };

  const selectIcd = (val, item) => {
    setIcd(item);
    setIcdId(val);
  };

  const handleClose = () => {
    props.history.push('/claim');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getEncounters({}));
    dispatch(getCoverageEligibilityResponses({}));
    dispatch(getPatients({}));
    dispatch(getInsurances({}));
    dispatch(getCoverages({}));
    dispatch(getClaimResponses({}));
    dispatch(getCareTeams({}));
    dispatch(getSupportingInfos({}));
    dispatch(getRelateds({}));
    dispatch(getInformationSequences({}));
    dispatch(getItems({}));
    dispatch(getDetailItems({}));
    dispatch(getSubDetailItems({}));
    dispatch(getClaims({}));
    dispatch(getOrganizations({}));
    dispatch(getPractitioners({}));
    dispatch(getPractitionerRoles({}));
    dispatch(getReferenceIdentifiers({}));
    dispatch(getPayees({}));
    dispatch(getLocations({}));
    dispatch(getAccidents({}));
    dispatch(getProducts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (products && diagnoses.length === 0) {
      const filter = [];
      const filterL = [];
      products.forEach(element => {
        if (element.startsWith('Diagnosis')) {
          filter.push(element);
        } else if (element.startsWith('LOINC')) {
          filterL.push(element);
        }
      });
      setDiagnoses(filter);
      setLoincs(filterL);
    }
  }, [products]);

  const saveEntity = values => {
    values.eligibilityOfflineDate = convertDateTimeToServer(values.eligibilityOfflineDate);
    values.authorizationOfflineDate = convertDateTimeToServer(values.authorizationOfflineDate);
    values.billableStart = convertDateTimeToServer(values.billableStart);
    values.billableEnd = convertDateTimeToServer(values.billableEnd);
    values.fundsReserve = values.fundsReserve === '' ? null : values.fundsReserve;

    switch (partyType) {
      case 'patient':
        values.payee = { type: 'Subscriber', partyPatient: null, partyOrganization: null };
        break;
      case 'organization':
        values.payee = { type: 'Provider', partyPatient: null, partyOrganization: null };
        break;
      default:
        values.payee = { type: 'Other', partyPatient: null, partyOrganization };
        break;
    }

    let entity = {
      ...claimEntity,
      ...values,
      relateds: relatedList,
      careTeams: careTeamList,
      insurances: insurancesList,
      diagnoses: diagnosisList,
      supportingInfos: supportingInfoList,
      items: itemList,
    };

    if (getOrgTerm(provider)) {
      entity = { ...entity, provider };
    }

    if (getPatTerm(patient)) {
      entity = { ...entity, patient };
    }

    if (getOrgTerm(insurer)) {
      entity = { ...entity, insurer };
    }

    if (getLocTerm(facility)) {
      entity = { ...entity, facility };
    }

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () => {
    if (!isNew) {
      return {
        ...claimEntity,
        use: claimEntity.use ?? 'Claim',
        type: claimEntity.type ?? 'Institutional',
        subType: claimEntity.subType ?? 'Ip',
        eligibilityOfflineDate: convertDateTimeFromServer(claimEntity.eligibilityOfflineDate),
        authorizationOfflineDate: convertDateTimeFromServer(claimEntity.authorizationOfflineDate),
        billableStart: convertDateTimeFromServer(claimEntity.billableStart),
        billableEnd: convertDateTimeFromServer(claimEntity.billableEnd),
        priority: claimEntity.priority ?? 'Stat',
        encounterId: claimEntity?.encounter?.id,
        eligibilityResponseId: claimEntity?.eligibilityResponse?.id,
        patientId: claimEntity?.patient?.id,
        providerId: claimEntity?.provider?.id,
        insurerId: claimEntity?.insurer?.id,
        prescription: claimEntity?.prescription,
        originalPrescription: claimEntity?.originalPrescription,
        payeeId: claimEntity?.payee?.id,
        referral: claimEntity?.referral,
        facilityId: claimEntity?.facility?.id,
        accidentId: claimEntity?.accident?.id,
      };
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hcpNphiesPortalApp.claim.home.createOrEditLabel" data-cy="ClaimCreateUpdateHeading">
            <Translate contentKey="hcpNphiesPortalApp.claim.home.createOrEditLabel">Create or edit a Claim</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={claimEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="claim-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.identifier')}
                id="claim-identifier"
                name="identifier"
                data-cy="identifier"
                type="text"
              />
              <ValidatedField label={translate('hcpNphiesPortalApp.claim.use')} id="claim-use" name="use" data-cy="use" type="select">
                <option value="Claim">{translate('hcpNphiesPortalApp.Use.Claim')}</option>
                <option value="PreAuthorization">{translate('hcpNphiesPortalApp.Use.PreAuthorization')}</option>
                <option value="Predetermination">{translate('hcpNphiesPortalApp.Use.Predetermination')}</option>
              </ValidatedField>
              <ValidatedField label={translate('hcpNphiesPortalApp.claim.type')} id="claim-type" name="type" data-cy="type" type="select">
                <option value="Institutional">{translate('hcpNphiesPortalApp.ClaimTypeEnum.Institutional')}</option>
                <option value="Oral">{translate('hcpNphiesPortalApp.ClaimTypeEnum.Oral')}</option>
                <option value="Pharmacy">{translate('hcpNphiesPortalApp.ClaimTypeEnum.Pharmacy')}</option>
                <option value="Professional">{translate('hcpNphiesPortalApp.ClaimTypeEnum.Professional')}</option>
                <option value="Vision">{translate('hcpNphiesPortalApp.ClaimTypeEnum.Vision')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.subType')}
                id="claim-subType"
                name="subType"
                data-cy="subType"
                type="select"
              >
                <option value="Ip">{translate('hcpNphiesPortalApp.ClaimSubTypeEnum.Ip')}</option>
                <option value="Op">{translate('hcpNphiesPortalApp.ClaimSubTypeEnum.Op')}</option>
                <option value="Emr">{translate('hcpNphiesPortalApp.ClaimSubTypeEnum.Emr')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.eligibilityOffline')}
                id="claim-eligibilityOffline"
                name="eligibilityOffline"
                data-cy="eligibilityOffline"
                type="text"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.eligibilityOfflineDate')}
                id="claim-eligibilityOfflineDate"
                name="eligibilityOfflineDate"
                data-cy="eligibilityOfflineDate"
                type="date"
                placeholder="YYYY-MM-DD"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.authorizationOfflineDate')}
                id="claim-authorizationOfflineDate"
                name="authorizationOfflineDate"
                data-cy="authorizationOfflineDate"
                type="date"
                placeholder="YYYY-MM-DD"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.billableStart')}
                id="claim-billableStart"
                name="billableStart"
                data-cy="billableStart"
                type="date"
                placeholder="YYYY-MM-DD"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.billableEnd')}
                id="claim-billableEnd"
                name="billableEnd"
                data-cy="billableEnd"
                type="date"
                placeholder="YYYY-MM-DD"
              />
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.priority')}
                id="claim-priority"
                name="priority"
                data-cy="priority"
                type="select"
              >
                <option value="Stat">{translate('hcpNphiesPortalApp.PriorityEnum.Stat')}</option>
                <option value="Normal">{translate('hcpNphiesPortalApp.PriorityEnum.Normal')}</option>
                <option value="Deferred">{translate('hcpNphiesPortalApp.PriorityEnum.Deferred')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('hcpNphiesPortalApp.claim.fundsReserve')}
                id="claim-fundsReserve"
                name="fundsReserve"
                data-cy="fundsReserve"
                type="select"
              >
                <option value="" key="0" />
                <option value="Patient">{translate('hcpNphiesPortalApp.FundsReserveEnum.Patient')}</option>
                <option value="Provider">{translate('hcpNphiesPortalApp.FundsReserveEnum.Provider')}</option>
                <option value="None">{translate('hcpNphiesPortalApp.FundsReserveEnum.None')}</option>
              </ValidatedField>
              <ValidatedField
                id="claim-encounter"
                name="encounterId"
                data-cy="encounter"
                label={translate('hcpNphiesPortalApp.claim.encounter')}
                type="select"
              >
                <option value="" key="0" />
                {encounters
                  ? encounters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="claim-eligibilityResponse"
                name="eligibilityResponseId"
                data-cy="eligibilityResponse"
                label={translate('hcpNphiesPortalApp.claim.eligibilityResponse')}
                type="select"
              >
                <option value="" key="0" />
                {coverageEligibilityResponses
                  ? coverageEligibilityResponses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormGroup>
                <Label for="claim-patient">
                  <Translate contentKey="hcpNphiesPortalApp.claim.patient">Patient</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchPatToTerm}
                    getItemValue={item => getPatTerm(item)}
                    items={[...patients]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getPatTerm(item)}</div>
                    )}
                    value={patientId}
                    onChange={changePatient}
                    onSelect={selectPatient}
                  />
                </FormGroup>
              </FormGroup>
              <FormGroup>
                <Label for="claim-provider">
                  <Translate contentKey="hcpNphiesPortalApp.claim.provider">Provider</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchOrgToTerm}
                    getItemValue={item => getOrgTerm(item)}
                    items={[...organizations]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getOrgTerm(item)}</div>
                    )}
                    value={provId}
                    onChange={changeProv}
                    onSelect={selectProv}
                  />
                </FormGroup>
              </FormGroup>
              <FormGroup>
                <Label for="claim-insurer">
                  <Translate contentKey="hcpNphiesPortalApp.claim.insurer">Insurer</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchOrgToTerm}
                    getItemValue={item => getOrgTerm(item)}
                    items={[...organizations]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getOrgTerm(item)}</div>
                    )}
                    value={insId}
                    onChange={changeIns}
                    onSelect={selectIns}
                  />
                </FormGroup>
              </FormGroup>
              <FormGroup>
                <Label for="claim-facility">
                  <Translate contentKey="hcpNphiesPortalApp.claim.facility">Facility</Translate>
                </Label>
                <FormGroup>
                  <Autocomplete
                    shouldItemRender={matchLocToTerm}
                    getItemValue={item => getLocTerm(item)}
                    items={[...locations]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getLocTerm(item)}</div>
                    )}
                    value={locId}
                    onChange={changeLoc}
                    onSelect={selectLoc}
                  />
                </FormGroup>
              </FormGroup>
              <div onChange={changePrescription}>
                {translate('hcpNphiesPortalApp.claim.prescription')}&nbsp;&nbsp;
                <input type="radio" value="1" checked={hasPrescription} /> Yes&nbsp;
                <input type="radio" value="0" checked={!hasPrescription} /> No&nbsp;
              </div>
              {hasPrescription ? (
                <div style={{ paddingLeft: '30px' }}>
                  <ValidatedField
                    id="prescription-ref"
                    name="prescription.ref"
                    data-cy="prescription.ref"
                    label={
                      translate('hcpNphiesPortalApp.claim.prescription') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.ref')
                    }
                    type="select"
                  >
                    <option value="" key="0" />
                    <option value="MedicationRequest" label="MedicationRequest" key="MedicationRequest" />
                    <option value="DeviceRequest" label="DeviceRequest" key="DeviceRequest" />
                    <option value="VisionPrescription" label="VisionPrescription" key="VisionPrescription" />
                  </ValidatedField>
                  <ValidatedField
                    label={
                      translate('hcpNphiesPortalApp.claim.prescription') +
                      ' ' +
                      translate('hcpNphiesPortalApp.referenceIdentifier.identifier')
                    }
                    id="prescription-identifier"
                    name="prescription.identifier"
                    data-cy="prescription.identifier"
                    type="text"
                  />
                  <ValidatedField
                    label={
                      translate('hcpNphiesPortalApp.claim.prescription') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.display')
                    }
                    id="prescription-display"
                    name="prescription.display"
                    data-cy="prescription.display"
                    type="text"
                  />
                </div>
              ) : null}
              <div onChange={changeOriginalPrescription}>
                {translate('hcpNphiesPortalApp.claim.originalPrescription')}&nbsp;&nbsp;
                <input type="radio" value="1" checked={hasOriginalPrescription} /> Yes&nbsp;
                <input type="radio" value="0" checked={!hasOriginalPrescription} /> No&nbsp;
              </div>
              {hasOriginalPrescription ? (
                <div style={{ paddingLeft: '30px' }}>
                  <ValidatedField
                    id="originalPrescription-ref"
                    name="originalPrescription.ref"
                    data-cy="originalPrescription.ref"
                    label={
                      translate('hcpNphiesPortalApp.claim.originalPrescription') +
                      ' ' +
                      translate('hcpNphiesPortalApp.referenceIdentifier.ref')
                    }
                    type="select"
                  >
                    <option value="" key="0" />
                    <option value="MedicationRequest" label="MedicationRequest" key="MedicationRequest" />
                  </ValidatedField>
                  <ValidatedField
                    label={
                      translate('hcpNphiesPortalApp.claim.originalPrescription') +
                      ' ' +
                      translate('hcpNphiesPortalApp.referenceIdentifier.identifier')
                    }
                    id="originalPrescription-identifier"
                    name="originalPrescription.identifier"
                    data-cy="originalPrescription.identifier"
                    type="text"
                  />
                  <ValidatedField
                    label={
                      translate('hcpNphiesPortalApp.claim.originalPrescription') +
                      ' ' +
                      translate('hcpNphiesPortalApp.referenceIdentifier.display')
                    }
                    id="originalPrescription-display"
                    name="originalPrescription.display"
                    data-cy="originalPrescription.display"
                    type="text"
                  />
                </div>
              ) : null}
              <div onChange={changePartyType}>
                Party&nbsp;&nbsp;
                <input type="radio" value="patient" checked={partyType === 'patient'} />{' '}
                {translate('hcpNphiesPortalApp.PayeeTypeEnum.Subscriber')}&nbsp;
                <input type="radio" value="organization" checked={partyType === 'organization'} />{' '}
                {translate('hcpNphiesPortalApp.PayeeTypeEnum.Provider')}&nbsp;
                <input type="radio" value="none" checked={partyType === 'none'} /> {translate('hcpNphiesPortalApp.PayeeTypeEnum.Other')}
                &nbsp;
              </div>
              {partyType === 'none' ? (
                <FormGroup style={{ paddingLeft: '30px' }}>
                  <Autocomplete
                    shouldItemRender={matchOrgToTerm}
                    getItemValue={item => getOrgTerm(item)}
                    items={[...organizations]}
                    renderItem={(item, isHighlighted) => (
                      <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getOrgTerm(item)}</div>
                    )}
                    value={partyOrganizationId}
                    onChange={changePartyOrganization}
                    onSelect={selectPartyOrganization}
                  />
                </FormGroup>
              ) : null}
              <div onChange={changeReferral}>
                {translate('hcpNphiesPortalApp.claim.referral')}&nbsp;&nbsp;
                <input type="radio" value="1" checked={hasReferral} /> Yes&nbsp;
                <input type="radio" value="0" checked={!hasReferral} /> No&nbsp;
              </div>
              {hasReferral ? (
                <div style={{ paddingLeft: '30px' }}>
                  <ValidatedField
                    id="referral-ref"
                    name="referral.ref"
                    data-cy="referral.ref"
                    label={translate('hcpNphiesPortalApp.claim.referral') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.ref')}
                    type="select"
                  >
                    <option value="" key="0" />
                    <option value="ServiceRequest" label="ServiceRequest" key="ServiceRequest" />
                  </ValidatedField>
                  <ValidatedField
                    label={
                      translate('hcpNphiesPortalApp.claim.referral') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.identifier')
                    }
                    id="referral-identifier"
                    name="referral.identifier"
                    data-cy="referral.identifier"
                    type="text"
                  />
                  <ValidatedField
                    label={
                      translate('hcpNphiesPortalApp.claim.referral') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.display')
                    }
                    id="referral-display"
                    name="referral.display"
                    data-cy="referral.display"
                    type="text"
                  />
                </div>
              ) : null}
              <div onChange={changeAccident}>
                {translate('hcpNphiesPortalApp.claim.accident')}&nbsp;&nbsp;
                <input type="radio" value="1" checked={hasAccident} /> Yes&nbsp;
                <input type="radio" value="0" checked={!hasAccident} /> No&nbsp;
              </div>
              {hasAccident ? (
                <div style={{ paddingLeft: '30px' }}>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.accident.date')}
                    id="accident-date"
                    name="accident.date"
                    data-cy="accident.date"
                    type="datetime-local"
                    placeholder="YYYY-MM-DD HH:mm"
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.accident.type')}
                    id="accident-type"
                    name="accident.type"
                    data-cy="accident.type"
                    type="select"
                  >
                    <option value="" key="0" />
                    <option value="MVA">{translate('hcpNphiesPortalApp.AccidentTypeEnum.MVA')}</option>
                    <option value="SCHOOL">{translate('hcpNphiesPortalApp.AccidentTypeEnum.SCHOOL')}</option>
                    <option value="SPT">{translate('hcpNphiesPortalApp.AccidentTypeEnum.SPT')}</option>
                    <option value="WPA">{translate('hcpNphiesPortalApp.AccidentTypeEnum.WPA')}</option>
                  </ValidatedField>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.address.addressLine')}
                    id="address-addressLine"
                    name="accident.location.addressLine"
                    data-cy="accident.location.addressLine"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.address.city')}
                    id="address-city"
                    name="accident.location.city"
                    data-cy="accident.location.city"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.address.district')}
                    id="address-district"
                    name="accident.location.district"
                    data-cy="accident.location.district"
                    type="text"
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.address.state')}
                    id="address-state"
                    name="accident.location.state"
                    data-cy="accident.location.state"
                    type="text"
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.address.postalCode')}
                    id="address-postalCode"
                    name="accident.location.postalCode"
                    data-cy="accident.location.postalCode"
                    type="text"
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                    }}
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.address.country')}
                    id="address-country"
                    name="accident.location.country"
                    data-cy="accident.location.country"
                    type="text"
                  />
                </div>
              ) : null}
              <Translate contentKey="hcpNphiesPortalApp.related.home.title">Related</Translate>
              <ul>
                {relatedList.map(e => (
                  <li style={{ padding: 10 }} key={e.name}>
                    {getRelatedTerm(e)}{' '}
                    <Button style={{ float: 'right' }} color="danger" onClick={() => removeRelated(relatedList.indexOf(e))}>
                      <Translate contentKey="entity.action.deleterelated">Delete related</Translate>
                    </Button>
                  </li>
                ))}
              </ul>
              <FormGroup>
                <Button color="primary" onClick={toggleRelated}>
                  <Translate contentKey="entity.action.addrelated">Add related</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenRelated} toggle={toggleRelated}>
                <Form>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.related.relationShip')}
                    id="related-relationShip"
                    name="relationShip"
                    data-cy="relationShip"
                    type="select"
                    onChange={handleRelatedChange}
                  >
                    <option value=""></option>
                    <option value="Prior">{translate('hcpNphiesPortalApp.ClaimRelationshipEnum.Prior')}</option>
                    <option value="Associated">{translate('hcpNphiesPortalApp.ClaimRelationshipEnum.Associated')}</option>
                    <option value="Extend">{translate('hcpNphiesPortalApp.ClaimRelationshipEnum.Extend')}</option>
                  </ValidatedField>
                  <div onChange={changeWithClaim}>
                    {translate('hcpNphiesPortalApp.related.claim')}&nbsp;&nbsp;
                    <input type="radio" value="1" checked={withClaimIdentifier} /> Identifier&nbsp;
                    <input type="radio" value="0" checked={!withClaimIdentifier} /> Reference&nbsp;
                  </div>
                  {withClaimIdentifier ? (
                    <div style={{ paddingLeft: '30px' }}>
                      <ValidatedField
                        id="claimReference-ref"
                        name="claimReference.ref"
                        data-cy="claimReference.ref"
                        label={
                          translate('hcpNphiesPortalApp.related.claimReference') +
                          ' ' +
                          translate('hcpNphiesPortalApp.referenceIdentifier.ref')
                        }
                        type="select"
                      >
                        <option value="Claim" label="Claim" key="Claim" />
                      </ValidatedField>
                      <ValidatedField
                        label={
                          translate('hcpNphiesPortalApp.related.claimReference') +
                          ' ' +
                          translate('hcpNphiesPortalApp.referenceIdentifier.identifier')
                        }
                        id="claimReference-identifier"
                        name="claimReference.identifier"
                        data-cy="claimReference.identifier"
                        type="text"
                        onChange={handleRelatedChange}
                      />
                    </div>
                  ) : (
                    <FormGroup style={{ paddingLeft: '30px' }}>
                      <Autocomplete
                        shouldItemRender={matchClaimToTerm}
                        getItemValue={item => getClaimTerm(item)}
                        items={[...claims]}
                        renderItem={(item, isHighlighted) => (
                          <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getClaimTerm(item)}</div>
                        )}
                        value={relatedClaimId}
                        onChange={changeRelatedClaim}
                        onSelect={selectRelatedClaim}
                      />
                    </FormGroup>
                  )}
                  <FormGroup>
                    <Button color="info" onClick={toggleRelated}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleRelatedSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Translate contentKey="hcpNphiesPortalApp.careTeam.home.title">CareTeam</Translate>
              <>
                <Button style={{ float: 'right' }} color="danger" onClick={() => removeCareTeam()}>
                  <Translate contentKey="entity.action.deletelatest">Delete CareTeam</Translate>
                </Button>
                <ul>
                  {careTeamList.map(e => (
                    <li style={{ padding: 10 }} key={e.name}>
                      {getCareTeamTerm(e)}
                    </li>
                  ))}
                </ul>
              </>
              <FormGroup>
                <Button color="primary" onClick={toggleCareTeam}>
                  <Translate contentKey="entity.action.addcareTeam">Add CareTeam</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenCareTeam} toggle={toggleCareTeam}>
                <Form>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.careTeam.sequence')}
                    id="care-team-sequence"
                    name="ctsequence"
                    data-cy="ctsequence"
                    type="text"
                    value={careTeamList.length + 1}
                    readOnly
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.careTeam.role')}
                    id="care-team-role"
                    name="role"
                    data-cy="role"
                    type="select"
                    onChange={handleCareTeamChange}
                  >
                    <option value=""></option>
                    <option value="Primary">{translate('hcpNphiesPortalApp.CareTeamRoleEnum.Primary')}</option>
                    <option value="Assist">{translate('hcpNphiesPortalApp.CareTeamRoleEnum.Assist')}</option>
                    <option value="Supervisor">{translate('hcpNphiesPortalApp.CareTeamRoleEnum.Supervisor')}</option>
                    <option value="Other">{translate('hcpNphiesPortalApp.CareTeamRoleEnum.Other')}</option>
                  </ValidatedField>

                  <ValidatedField
                    label="Qualification"
                    id="care-team-qualification"
                    name="qualification"
                    data-cy="qualification"
                    type="select"
                    onChange={handleCareTeamChange}
                  >
                    <option value=""></option>
                    <option value="DOT01_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_00')}</option>
                    <option value="DOT01_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_01')}</option>
                    <option value="DOT01_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_02')}</option>
                    <option value="DOT01_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_03')}</option>
                    <option value="DOT01_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_04')}</option>
                    <option value="DOT01_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_05')}</option>
                    <option value="DOT01_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_06')}</option>
                    <option value="DOT01_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_07')}</option>
                    <option value="DOT01_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT01_08')}</option>
                    <option value="DOT02_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT02_00')}</option>
                    <option value="DOT02_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT02_01')}</option>
                    <option value="DOT03_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT03_00')}</option>
                    <option value="DOT03_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT03_01')}</option>
                    <option value="DOT03_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT03_02')}</option>
                    <option value="DOT03_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT03_03')}</option>
                    <option value="DOT04_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT04_00')}</option>
                    <option value="DOT04_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT04_01')}</option>
                    <option value="DOT04_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT04_02')}</option>
                    <option value="DOT05_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_00')}</option>
                    <option value="DOT05_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_01')}</option>
                    <option value="DOT05_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_02')}</option>
                    <option value="DOT05_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_03')}</option>
                    <option value="DOT05_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_04')}</option>
                    <option value="DOT05_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_05')}</option>
                    <option value="DOT05_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_06')}</option>
                    <option value="DOT05_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_07')}</option>
                    <option value="DOT05_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_08')}</option>
                    <option value="DOT05_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_09')}</option>
                    <option value="DOT05_10">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT05_10')}</option>
                    <option value="DOT06_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT06_00')}</option>
                    <option value="DOT06_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT06_01')}</option>
                    <option value="DOT06_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT06_02')}</option>
                    <option value="DOT06_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT06_03')}</option>
                    <option value="DOT06_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT06_04')}</option>
                    <option value="DOT06_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT06_05')}</option>
                    <option value="DOT07_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT07_00')}</option>
                    <option value="DOT08_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_00')}</option>
                    <option value="DOT08_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_01')}</option>
                    <option value="DOT08_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_02')}</option>
                    <option value="DOT08_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_03')}</option>
                    <option value="DOT08_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_04')}</option>
                    <option value="DOT08_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_05')}</option>
                    <option value="DOT08_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_06')}</option>
                    <option value="DOT08_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_07')}</option>
                    <option value="DOT08_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_08')}</option>
                    <option value="DOT08_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_09')}</option>
                    <option value="DOT08_10">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_10')}</option>
                    <option value="DOT08_11">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_11')}</option>
                    <option value="DOT08_12">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_12')}</option>
                    <option value="DOT08_13">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_13')}</option>
                    <option value="DOT08_14">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_14')}</option>
                    <option value="DOT08_15">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_15')}</option>
                    <option value="DOT08_16">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_16')}</option>
                    <option value="DOT08_17">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_17')}</option>
                    <option value="DOT08_18">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_18')}</option>
                    <option value="DOT08_19">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_19')}</option>
                    <option value="DOT08_20">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_20')}</option>
                    <option value="DOT08_21">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_21')}</option>
                    <option value="DOT08_22">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_22')}</option>
                    <option value="DOT08_23">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_23')}</option>
                    <option value="DOT08_24">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_24')}</option>
                    <option value="DOT08_25">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_25')}</option>
                    <option value="DOT08_26">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT08_26')}</option>
                    <option value="DOT09_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT09_00')}</option>
                    <option value="DOT10_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_00')}</option>
                    <option value="DOT10_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_01')}</option>
                    <option value="DOT10_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_02')}</option>
                    <option value="DOT10_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_03')}</option>
                    <option value="DOT10_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_04')}</option>
                    <option value="DOT10_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_05')}</option>
                    <option value="DOT10_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_06')}</option>
                    <option value="DOT10_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_07')}</option>
                    <option value="DOT10_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_08')}</option>
                    <option value="DOT10_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT10_09')}</option>
                    <option value="DOT11_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_00')}</option>
                    <option value="DOT11_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_01')}</option>
                    <option value="DOT11_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_02')}</option>
                    <option value="DOT11_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_03')}</option>
                    <option value="DOT11_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_04')}</option>
                    <option value="DOT11_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_05')}</option>
                    <option value="DOT11_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_06')}</option>
                    <option value="DOT11_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_07')}</option>
                    <option value="DOT11_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_08')}</option>
                    <option value="DOT11_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_09')}</option>
                    <option value="DOT11_10">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_10')}</option>
                    <option value="DOT11_11">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_11')}</option>
                    <option value="DOT11_12">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_12')}</option>
                    <option value="DOT11_13">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_13')}</option>
                    <option value="DOT11_14">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_14')}</option>
                    <option value="DOT11_15">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_15')}</option>
                    <option value="DOT11_16">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT11_16')}</option>
                    <option value="DOT12_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT12_00')}</option>
                    <option value="DOT12_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT12_01')}</option>
                    <option value="DOT12_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT12_02')}</option>
                    <option value="DOT12_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT12_03')}</option>
                    <option value="DOT12_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT12_04')}</option>
                    <option value="DOT13_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_00')}</option>
                    <option value="DOT13_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_01')}</option>
                    <option value="DOT13_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_02')}</option>
                    <option value="DOT13_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_03')}</option>
                    <option value="DOT13_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_04')}</option>
                    <option value="DOT13_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_05')}</option>
                    <option value="DOT13_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_06')}</option>
                    <option value="DOT13_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT13_07')}</option>
                    <option value="DOT14_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_00')}</option>
                    <option value="DOT14_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_01')}</option>
                    <option value="DOT14_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_02')}</option>
                    <option value="DOT14_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_03')}</option>
                    <option value="DOT14_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_04')}</option>
                    <option value="DOT14_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_05')}</option>
                    <option value="DOT14_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_06')}</option>
                    <option value="DOT14_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_07')}</option>
                    <option value="DOT14_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_08')}</option>
                    <option value="DOT14_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_09')}</option>
                    <option value="DOT14_10">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_10')}</option>
                    <option value="DOT14_11">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_11')}</option>
                    <option value="DOT14_12">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_12')}</option>
                    <option value="DOT14_13">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_13')}</option>
                    <option value="DOT14_14">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_14')}</option>
                    <option value="DOT14_15">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_15')}</option>
                    <option value="DOT14_16">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_16')}</option>
                    <option value="DOT14_17">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_17')}</option>
                    <option value="DOT14_18">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_18')}</option>
                    <option value="DOT14_19">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_19')}</option>
                    <option value="DOT14_20">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT14_20')}</option>
                    <option value="DOT15_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT15_00')}</option>
                    <option value="DOT15_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT15_01')}</option>
                    <option value="DOT15_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT15_02')}</option>
                    <option value="DOT15_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT15_03')}</option>
                    <option value="DOT15_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT15_04')}</option>
                    <option value="DOT15_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT15_05')}</option>
                    <option value="DOT15_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT15_06')}</option>
                    <option value="DOT16_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT16_00')}</option>
                    <option value="DOT16_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT16_01')}</option>
                    <option value="DOT16_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT16_02')}</option>
                    <option value="DOT17_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_00')}</option>
                    <option value="DOT17_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_01')}</option>
                    <option value="DOT17_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_02')}</option>
                    <option value="DOT17_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_03')}</option>
                    <option value="DOT17_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_04')}</option>
                    <option value="DOT17_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_05')}</option>
                    <option value="DOT17_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_06')}</option>
                    <option value="DOT17_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_07')}</option>
                    <option value="DOT17_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_08')}</option>
                    <option value="DOT17_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_09')}</option>
                    <option value="DOT17_10">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_10')}</option>
                    <option value="DOT17_11">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_11')}</option>
                    <option value="DOT17_12">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT17_12')}</option>
                    <option value="DOT18_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_00')}</option>
                    <option value="DOT18_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_01')}</option>
                    <option value="DOT18_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_02')}</option>
                    <option value="DOT18_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_03')}</option>
                    <option value="DOT18_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_04')}</option>
                    <option value="DOT18_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_05')}</option>
                    <option value="DOT18_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_06')}</option>
                    <option value="DOT18_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_07')}</option>
                    <option value="DOT18_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_08')}</option>
                    <option value="DOT18_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_09')}</option>
                    <option value="DOT18_10">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_10')}</option>
                    <option value="DOT18_11">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_11')}</option>
                    <option value="DOT18_12">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT18_12')}</option>
                    <option value="DOT19_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_00')}</option>
                    <option value="DOT19_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_01')}</option>
                    <option value="DOT19_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_02')}</option>
                    <option value="DOT19_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_03')}</option>
                    <option value="DOT19_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_04')}</option>
                    <option value="DOT19_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_05')}</option>
                    <option value="DOT19_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_06')}</option>
                    <option value="DOT19_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_07')}</option>
                    <option value="DOT19_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_08')}</option>
                    <option value="DOT19_09">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_09')}</option>
                    <option value="DOT19_10">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_10')}</option>
                    <option value="DOT19_11">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_11')}</option>
                    <option value="DOT19_12">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_12')}</option>
                    <option value="DOT19_13">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_13')}</option>
                    <option value="DOT19_14">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_14')}</option>
                    <option value="DOT19_15">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_15')}</option>
                    <option value="DOT19_16">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_16')}</option>
                    <option value="DOT19_17">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_17')}</option>
                    <option value="DOT19_18">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_18')}</option>
                    <option value="DOT19_19">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_19')}</option>
                    <option value="DOT19_20">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_20')}</option>
                    <option value="DOT19_21">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_21')}</option>
                    <option value="DOT19_22">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_22')}</option>
                    <option value="DOT19_23">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_23')}</option>
                    <option value="DOT19_24">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_24')}</option>
                    <option value="DOT19_25">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_25')}</option>
                    <option value="DOT19_26">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT19_26')}</option>
                    <option value="DOT20_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT20_00')}</option>
                    <option value="DOT20_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT20_01')}</option>
                    <option value="DOT20_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT20_02')}</option>
                    <option value="DOT20_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT20_03')}</option>
                    <option value="DOT20_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT20_04')}</option>
                    <option value="DOT20_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT20_05')}</option>
                    <option value="DOT20_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT20_06')}</option>
                    <option value="DOT21_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT21_00')}</option>
                    <option value="DOT21_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT21_01')}</option>
                    <option value="DOT21_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT21_02')}</option>
                    <option value="DOT22_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_00')}</option>
                    <option value="DOT22_01">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_01')}</option>
                    <option value="DOT22_02">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_02')}</option>
                    <option value="DOT22_03">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_03')}</option>
                    <option value="DOT22_04">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_04')}</option>
                    <option value="DOT22_05">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_05')}</option>
                    <option value="DOT22_06">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_06')}</option>
                    <option value="DOT22_07">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_07')}</option>
                    <option value="DOT22_08">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT22_08')}</option>
                    <option value="DOT23_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT23_00')}</option>
                    <option value="DOT24_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT24_00')}</option>
                    <option value="DOT25_00">{translate('hcpNphiesPortalApp.SpecialtyEnum.DOT25_00')}</option>
                  </ValidatedField>
                  <div onChange={changeWithoutRole}>
                    {translate('hcpNphiesPortalApp.careTeam.provider')}&nbsp;&nbsp;
                    <input type="radio" value="1" checked={withoutRole} /> Practitioner&nbsp;
                    <input type="radio" value="0" checked={!withoutRole} /> PractitionerRole&nbsp;
                  </div>
                  {withoutRole ? (
                    <FormGroup style={{ paddingLeft: '30px' }}>
                      <Autocomplete
                        shouldItemRender={matchPractitionerToTerm}
                        getItemValue={item => getPractitionerTerm(item)}
                        items={[...practitioners]}
                        renderItem={(item, isHighlighted) => (
                          <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getPractitionerTerm(item)}</div>
                        )}
                        value={careTeamPractitionerId}
                        onChange={changeCareTeamPractitioner}
                        onSelect={selectCareTeamPractitioner}
                      />
                    </FormGroup>
                  ) : (
                    <FormGroup style={{ paddingLeft: '30px' }}>
                      <Autocomplete
                        shouldItemRender={matchPractitionerRoleToTerm}
                        getItemValue={item => getPractitionerRoleTerm(item)}
                        items={[...practitionerRoles]}
                        renderItem={(item, isHighlighted) => (
                          <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getPractitionerRoleTerm(item)}</div>
                        )}
                        value={careTeamPractitionerRoleId}
                        onChange={changeCareTeamPractitionerRole}
                        onSelect={selectCareTeamPractitionerRole}
                      />
                    </FormGroup>
                  )}
                  <FormGroup>
                    <Button color="info" onClick={toggleCareTeam}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleCareTeamSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Translate contentKey="hcpNphiesPortalApp.insurance.home.title">Insurances</Translate>
              <>
                <Button style={{ float: 'right' }} color="danger" onClick={() => removeInsurances()}>
                  <Translate contentKey="entity.action.deletelatest">Delete Insurance</Translate>
                </Button>
                <ul>
                  {insurancesList.map(e => (
                    <li style={{ padding: 10 }} key={e.name}>
                      {getInsurancesTerm(e)}{' '}
                    </li>
                  ))}
                </ul>
              </>
              <FormGroup>
                <Button color="primary" onClick={toggleInsurances}>
                  <Translate contentKey="entity.action.addinsurance">Add Insurance</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenInsurances} toggle={toggleInsurances}>
                <Form>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.insurance.sequence')}
                    id="insurances-sequence"
                    name="inssequence"
                    data-cy="inssequence"
                    type="text"
                    value={insurancesList.length + 1}
                    readOnly
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.insurance.focal')}
                    id="insurance-focal"
                    name="focal"
                    data-cy="focal"
                    check
                    type="checkbox"
                    onChange={handleInsurancesChange}
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.insurance.preAuthRef')}
                    id="insurance-preAuthRef"
                    name="preAuthRef"
                    data-cy="preAuthRef"
                    type="text"
                    onChange={handleInsurancesChange}
                  />
                  <FormGroup>
                    <Label for="insurance-coverage">Coverage</Label>
                    <FormGroup>
                      <Autocomplete
                        shouldItemRender={matchCovToTerm}
                        getItemValue={item => getCovTerm(item)}
                        items={[...coverages]}
                        renderItem={(item, isHighlighted) => (
                          <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getCovTerm(item)}</div>
                        )}
                        value={coverageId}
                        onChange={changeCoverage}
                        onSelect={selectCoverage}
                      />
                    </FormGroup>
                  </FormGroup>
                  <FormGroup>
                    <Label for="insurance-cr">Claim Response</Label>
                    <FormGroup>
                      <Autocomplete
                        shouldItemRender={matchCrToTerm}
                        getItemValue={item => getCrTerm(item)}
                        items={[...claimResponses]}
                        renderItem={(item, isHighlighted) => (
                          <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getCrTerm(item)}</div>
                        )}
                        value={crId}
                        onChange={changeCr}
                        onSelect={selectCr}
                      />
                    </FormGroup>
                  </FormGroup>
                  <FormGroup>
                    <Button color="info" onClick={toggleInsurances}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleInsurancesSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Translate contentKey="hcpNphiesPortalApp.diagnosis.home.title">Diagnosis</Translate>
              <>
                <Button style={{ float: 'right' }} color="danger" onClick={() => removeDiagnosis()}>
                  <Translate contentKey="entity.action.deletelatest">Delete Diagnosis</Translate>
                </Button>
                <ul>
                  {diagnosisList.map(e => (
                    <li style={{ padding: 10 }} key={e.name}>
                      {getDiagnosisTerm(e)}{' '}
                    </li>
                  ))}
                </ul>
              </>
              <FormGroup>
                <Button color="primary" onClick={toggleDiagnosis}>
                  <Translate contentKey="entity.action.adddiagnosis">Add Diagnosis</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenDiagnosis} toggle={toggleDiagnosis}>
                <Form>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.diagnosis.sequence')}
                    id="diagnosis-sequence"
                    name="diagsequence"
                    data-cy="diagsequence"
                    type="text"
                    value={diagnosisList.length + 1}
                    readOnly
                  />
                  <FormGroup>
                    <Label for="diagnosis-diagnosis">Diagnosis</Label>
                    <FormGroup>
                      <Autocomplete
                        shouldItemRender={matchProdToTerm}
                        getItemValue={item => getProdTerm(item)}
                        items={[...diagnoses]}
                        renderItem={(item, isHighlighted) => (
                          <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getProdTerm(item)}</div>
                        )}
                        value={diagnosisId}
                        onChange={changeDiagnosis}
                        onSelect={selectDiagnosis}
                      />
                    </FormGroup>
                  </FormGroup>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.diagnosis.type')}
                    id="diagnosis-type"
                    name="type"
                    data-cy="type"
                    type="select"
                    onChange={handleDiagnosisChange}
                  >
                    <option value=""></option>
                    <option value="Admitting">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Admitting')}</option>
                    <option value="Clinical">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Clinical')}</option>
                    <option value="Differential">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Differential')}</option>
                    <option value="Discharge">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Discharge')}</option>
                    <option value="Laboratory">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Laboratory')}</option>
                    <option value="Nursing">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Nursing')}</option>
                    <option value="Prenatal">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Prenatal')}</option>
                    <option value="Principal">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Principal')}</option>
                    <option value="Radiology">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Radiology')}</option>
                    <option value="Remote">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Remote')}</option>
                    <option value="Retrospective">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Retrospective')}</option>
                    <option value="Self">{translate('hcpNphiesPortalApp.DiagnosisTypeEnum.Self')}</option>
                  </ValidatedField>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.diagnosis.onAdmission')}
                    id="diagnosis-onAdmission"
                    name="onAdmission"
                    data-cy="onAdmission"
                    type="select"
                    onChange={handleDiagnosisChange}
                  >
                    <option value=""></option>
                    <option value="Y">{translate('hcpNphiesPortalApp.DiagnosisOnAdmissionEnum.Y')}</option>
                    <option value="N">{translate('hcpNphiesPortalApp.DiagnosisOnAdmissionEnum.N')}</option>
                    <option value="U">{translate('hcpNphiesPortalApp.DiagnosisOnAdmissionEnum.U')}</option>
                  </ValidatedField>
                  <FormGroup>
                    <Button color="info" onClick={toggleDiagnosis}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleDiagnosisSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Translate contentKey="hcpNphiesPortalApp.supportingInfo.home.title">SupportingInfo</Translate>
              {supportingInfoList.length > 0 ? (
                <>
                  <Button style={{ float: 'right' }} color="danger" onClick={() => removeSupportingInfo()}>
                    <Translate contentKey="entity.action.deletelatest">Delete SupportingInfo</Translate>
                  </Button>
                  <ul>
                    {supportingInfoList.map(e => (
                      <li style={{ padding: 10 }} key={e.name}>
                        {getSupportingInfoTerm(e)}{' '}
                      </li>
                    ))}
                  </ul>
                </>
              ) : null}
              <FormGroup>
                <Button color="primary" onClick={toggleSupportingInfo}>
                  <Translate contentKey="entity.action.addsupportingInfo">Add SupportingInfo</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenSupportingInfo} toggle={toggleSupportingInfo}>
                <Form>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.supportingInfo.sequence')}
                    id="supportingInfo-sequence"
                    name="supsequence"
                    data-cy="supsequence"
                    type="text"
                    value={supportingInfoList.length + 1}
                    readOnly
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.supportingInfo.category')}
                    id="supporting-info-category"
                    name="category"
                    data-cy="category"
                    type="select"
                    onChange={handleSupportingInfoChange}
                  >
                    <option value=""></option>
                    <option value="Info">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Info')}</option>
                    <option value="Onset">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Onset')}</option>
                    <option value="Attachment">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Attachment')}</option>
                    <option value="Missingtooth">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Missingtooth')}</option>
                    <option value="Hospitalized">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Hospitalized')}</option>
                    <option value="EmploymentImpacted">
                      {translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.EmploymentImpacted')}
                    </option>
                    <option value="Patient_Reason_for_Visit">
                      {translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Patient_Reason_for_Visit')}
                    </option>
                    <option value="Lab_test">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Lab_test')}</option>
                    <option value="Reason_for_Visit">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Reason_for_Visit')}</option>
                    <option value="Days_Supply">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Days_Supply')}</option>
                    <option value="Vital_Sign_Weight">
                      {translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Vital_Sign_Weight')}
                    </option>
                    <option value="Vital_Sign_Systolic">
                      {translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Vital_Sign_Systolic')}
                    </option>
                    <option value="Vital_Sign_Diastolic">
                      {translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Vital_Sign_Diastolic')}
                    </option>
                    <option value="Icu_hours">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Icu_hours')}</option>
                    <option value="Ventilation_hours">
                      {translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Ventilation_hours')}
                    </option>
                    <option value="Vital_Sign_Height">
                      {translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Vital_Sign_Height')}
                    </option>
                    <option value="Chief_Complaint">{translate('hcpNphiesPortalApp.SupportingInfoCategoryEnum.Chief_Complaint')}</option>
                  </ValidatedField>
                  {/* <ValidatedField
                    label="Code"
                    id="supportingInfo-code-type"
                    name="codeType"
                    data-cy="codeType"
                    type="select"
                    onChange={changeInfoCodeType}
                  >
                    <option value="0">Visit</option>
                    <option value="1">FDI oral</option>
                    <option value="2">LOINC</option>
                  </ValidatedField> */}
                  {infoCodeType === 0 ? (
                    <ValidatedField
                      label={translate('hcpNphiesPortalApp.supportingInfo.codeVisit')}
                      id="supporting-info-codeVisit"
                      name="codeVisit"
                      data-cy="codeVisit"
                      type="select"
                    >
                      <option value=""></option>
                      <option value="New_visit">{translate('hcpNphiesPortalApp.SupportingInfoCodeVisitEnum.New_visit')}</option>
                      <option value="Follow_up">{translate('hcpNphiesPortalApp.SupportingInfoCodeVisitEnum.Follow_up')}</option>
                      <option value="Refill">{translate('hcpNphiesPortalApp.SupportingInfoCodeVisitEnum.Refill')}</option>
                      <option value="Walk_in">{translate('hcpNphiesPortalApp.SupportingInfoCodeVisitEnum.Walk_in')}</option>
                      <option value="Referral">{translate('hcpNphiesPortalApp.SupportingInfoCodeVisitEnum.Referral')}</option>
                    </ValidatedField>
                  ) : infoCodeType === 1 ? (
                    <ValidatedField
                      label={translate('hcpNphiesPortalApp.supportingInfo.codeFdiOral')}
                      id="supporting-info-codeFdiOral"
                      name="codeFdiOral"
                      data-cy="codeFdiOral"
                      type="select"
                    >
                      <option value=""></option>
                      <option value="N11">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N11')}</option>
                      <option value="N12">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N12')}</option>
                      <option value="N13">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N13')}</option>
                      <option value="N14">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N14')}</option>
                      <option value="N15">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N15')}</option>
                      <option value="N16">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N16')}</option>
                      <option value="N17">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N17')}</option>
                      <option value="N18">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N18')}</option>
                      <option value="N21">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N21')}</option>
                      <option value="N22">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N22')}</option>
                      <option value="N23">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N23')}</option>
                      <option value="N24">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N24')}</option>
                      <option value="N25">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N25')}</option>
                      <option value="N26">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N26')}</option>
                      <option value="N27">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N27')}</option>
                      <option value="N28">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N28')}</option>
                      <option value="N31">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N31')}</option>
                      <option value="N32">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N32')}</option>
                      <option value="N33">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N33')}</option>
                      <option value="N34">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N34')}</option>
                      <option value="N35">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N35')}</option>
                      <option value="N36">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N36')}</option>
                      <option value="N37">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N37')}</option>
                      <option value="N38">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N38')}</option>
                      <option value="N41">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N41')}</option>
                      <option value="N42">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N42')}</option>
                      <option value="N43">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N43')}</option>
                      <option value="N44">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N44')}</option>
                      <option value="N45">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N45')}</option>
                      <option value="N46">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N46')}</option>
                      <option value="N47">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N47')}</option>
                      <option value="N48">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N48')}</option>
                      <option value="N51">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N51')}</option>
                      <option value="N52">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N52')}</option>
                      <option value="N53">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N53')}</option>
                      <option value="N54">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N54')}</option>
                      <option value="N55">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N55')}</option>
                      <option value="N61">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N61')}</option>
                      <option value="N62">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N62')}</option>
                      <option value="N63">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N63')}</option>
                      <option value="N64">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N64')}</option>
                      <option value="N65">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N65')}</option>
                      <option value="N71">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N71')}</option>
                      <option value="N72">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N72')}</option>
                      <option value="N73">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N73')}</option>
                      <option value="N74">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N74')}</option>
                      <option value="N75">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N75')}</option>
                      <option value="N81">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N81')}</option>
                      <option value="N82">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N82')}</option>
                      <option value="N83">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N83')}</option>
                      <option value="N84">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N84')}</option>
                      <option value="N85">{translate('hcpNphiesPortalApp.SupportingInfoCodeFdiEnum.N85')}</option>
                    </ValidatedField>
                  ) : infoCodeType === 2 ? (
                    <FormGroup>
                      <Label for="supportingInfo-loinc">LOINC</Label>
                      <FormGroup>
                        <Autocomplete
                          shouldItemRender={matchProdToTerm}
                          getItemValue={item => getProdTerm(item)}
                          items={[...loincs]}
                          renderItem={(item, isHighlighted) => (
                            <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getProdTerm(item)}</div>
                          )}
                          value={loincId}
                          onChange={changeLoinc}
                          onSelect={selectLoinc}
                        />
                      </FormGroup>
                    </FormGroup>
                  ) : infoCodeType === 3 ? (
                    <FormGroup>
                      <Label for="supportingInfo-icd">ICD</Label>
                      <FormGroup>
                        <Autocomplete
                          shouldItemRender={matchProdToTerm}
                          getItemValue={item => getProdTerm(item)}
                          items={[...diagnoses]}
                          renderItem={(item, isHighlighted) => (
                            <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getProdTerm(item)}</div>
                          )}
                          value={icdId}
                          onChange={changeIcd}
                          onSelect={selectIcd}
                        />
                      </FormGroup>
                    </FormGroup>
                  ) : null}
                  {timing > 0 ? (
                    <ValidatedField
                      label={translate('hcpNphiesPortalApp.supportingInfo.timing')}
                      id="supporting-info-timing"
                      name="timing"
                      data-cy="timing"
                      type="datetime-local"
                      placeholder="YYYY-MM-DD HH:mm"
                      onChange={handleSupportingInfoChange}
                    />
                  ) : null}
                  {timing > 1 ? (
                    <ValidatedField
                      label={translate('hcpNphiesPortalApp.supportingInfo.timingEnd')}
                      id="supporting-info-timingEnd"
                      name="timingEnd"
                      data-cy="timingEnd"
                      type="datetime-local"
                      placeholder="YYYY-MM-DD HH:mm"
                      onChange={handleSupportingInfoChange}
                    />
                  ) : null}
                  {infoValueType === 0 ? (
                    <ValidatedField
                      label="Value"
                      id="supporting-info-valueString"
                      name="valueString"
                      data-cy="valueString"
                      type="text"
                      onChange={handleSupportingInfoChange}
                    />
                  ) : infoValueType === 1 ? (
                    <ValidatedField
                      id="supporting-info-valueBoolean"
                      name="valueBoolean"
                      data-cy="valueBoolean"
                      check
                      type="checkbox"
                      onChange={handleSupportingInfoChange}
                    />
                  ) : infoValueType === 2 ? (
                    <div>
                      <ValidatedField
                        label={unit === '-1' ? 'Quantity' : 'Quantity (' + unit + ')'}
                        id="supporting-info-valueQuantity-value"
                        name="valueQuantity.value"
                        data-cy="valueQuantity.value"
                        type="text"
                        onChange={handleSupportingInfoChange}
                        validate={{
                          validate: v => isNumber(v) || translate('entity.validation.number'),
                        }}
                      />
                      {unit === '-1' ? (
                        <ValidatedField
                          label="Unit"
                          id="supportingInfo-code-type"
                          name="codeType"
                          data-cy="codeType"
                          type="select"
                          onChange={changeUnit}
                        >
                          <option value=""></option>
                          <option value="g">g</option>
                          <option value="L">L</option>
                        </ValidatedField>
                      ) : null}
                    </div>
                  ) : infoValueType === 3 ? (
                    <>
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.attachment.contentType')}
                        id="attachment-contentType"
                        name="valueAttachment.contentType"
                        data-cy="valueAttachment.contentType"
                        type="text"
                        onChange={handleSupportingInfoChange}
                      />
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.attachment.title')}
                        id="attachment-title"
                        name="valueAttachment.title"
                        data-cy="valueAttachment.title"
                        type="text"
                        onChange={handleSupportingInfoChange}
                      />
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.attachment.language')}
                        id="attachment-language"
                        name="valueAttachment.language"
                        data-cy="valueAttachment.language"
                        type="select"
                        onChange={handleSupportingInfoChange}
                      >
                        <option value=""></option>
                        <option value="AR">{translate('hcpNphiesPortalApp.LanguageEnum.AR')}</option>
                        <option value="EN">{translate('hcpNphiesPortalApp.LanguageEnum.EN')}</option>
                      </ValidatedField>
                      <div onChange={changeWithData}>
                        <input type="radio" value="1" checked={withData} /> Data&nbsp;
                        <input type="radio" value="0" checked={!withData} /> Url&nbsp;
                      </div>
                      {withData ? (
                        <ValidatedBlobField
                          label={translate('hcpNphiesPortalApp.attachment.dataFile')}
                          id="attachment-dataFile"
                          name="valueAttachment.dataFile"
                          data-cy="valueAttachment.dataFile"
                          openActionLabel={translate('entity.action.open')}
                          onChange={handleSupportingInfoChange}
                        />
                      ) : (
                        <>
                          <ValidatedField
                            label={translate('hcpNphiesPortalApp.attachment.url')}
                            id="attachment-url"
                            name="valueAttachment.url"
                            data-cy="valueAttachment.url"
                            type="text"
                            onChange={handleSupportingInfoChange}
                          />
                          <ValidatedField
                            label={translate('hcpNphiesPortalApp.attachment.attachmentSize')}
                            id="attachment-attachmentSize"
                            name="valueAttachment.attachmentSize"
                            data-cy="valueAttachment.attachmentSize"
                            type="text"
                            onChange={handleSupportingInfoChange}
                          />
                          <ValidatedBlobField
                            label={translate('hcpNphiesPortalApp.attachment.hash')}
                            id="attachment-hash"
                            name="valueAttachment.hash"
                            data-cy="valueAttachment.hash"
                            openActionLabel={translate('entity.action.open')}
                            onChange={handleSupportingInfoChange}
                          />
                        </>
                      )}
                    </>
                  ) : infoValueType === 4 ? (
                    <>
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.referenceIdentifier.ref')}
                        id="reference-identifier-ref"
                        name="valueReference.ref"
                        data-cy="valueReference.ref"
                        type="text"
                        onChange={handleSupportingInfoChange}
                      />
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.referenceIdentifier.identifier')}
                        id="reference-identifier-identifier"
                        name="valueReference.identifier"
                        data-cy="valueReference.identifier"
                        type="text"
                        onChange={handleSupportingInfoChange}
                      />
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.referenceIdentifier.display')}
                        id="reference-identifier-display"
                        name="valueReference.display"
                        data-cy="valueReference.display"
                        type="text"
                        onChange={handleSupportingInfoChange}
                      />
                    </>
                  ) : null}
                  {isMissingTooth ? (
                    <ValidatedField
                      label={translate('hcpNphiesPortalApp.supportingInfo.reasonMissingTooth')}
                      id="supporting-info-reasonMissingTooth"
                      name="reasonMissingTooth"
                      data-cy="reasonMissingTooth"
                      type="select"
                      onChange={handleSupportingInfoChange}
                    >
                      <option value=""></option>
                      <option value="E">{translate('hcpNphiesPortalApp.SupportingInfoReasonMissingToothEnum.E')}</option>
                      <option value="C">{translate('hcpNphiesPortalApp.SupportingInfoReasonMissingToothEnum.C')}</option>
                      <option value="U">{translate('hcpNphiesPortalApp.SupportingInfoReasonMissingToothEnum.U')}</option>
                      <option value="O">{translate('hcpNphiesPortalApp.SupportingInfoReasonMissingToothEnum.O')}</option>
                    </ValidatedField>
                  ) : (
                    <ValidatedField
                      label={translate('hcpNphiesPortalApp.supportingInfo.reason')}
                      id="supporting-info-reason"
                      name="reason"
                      data-cy="reason"
                      type="select"
                      onChange={handleSupportingInfoChange}
                    >
                      <option value=""></option>
                      <option value="Missing_info">{translate('hcpNphiesPortalApp.SupportingInfoReasonEnum.Missing_info')}</option>
                      <option value="Missing_attach">{translate('hcpNphiesPortalApp.SupportingInfoReasonEnum.Missing_attach')}</option>
                      <option value="Info_Correct">{translate('hcpNphiesPortalApp.SupportingInfoReasonEnum.Info_Correct')}</option>
                    </ValidatedField>
                  )}
                  <FormGroup>
                    <Button color="info" onClick={toggleSupportingInfo}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleSupportingInfoSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Translate contentKey="hcpNphiesPortalApp.item.home.title">Items</Translate>
              <>
                <Button style={{ float: 'right' }} color="danger" onClick={() => removeItem()}>
                  <Translate contentKey="entity.action.deletelatest">Delete item</Translate>
                </Button>
                <ul>
                  {itemList.map(e => (
                    <li style={{ padding: 10 }} key={e.name}>
                      {getItemTerm(e)}{' '}
                    </li>
                  ))}
                </ul>
              </>
              <FormGroup>
                <Button color="primary" onClick={toggleItem}>
                  <Translate contentKey="entity.action.additem">Add item</Translate>
                </Button>
              </FormGroup>
              <MyModal isOpen={isOpenItem} toggle={toggleItem}>
                <Form>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.sequence')}
                    id="item-sequence"
                    name="itemsequence"
                    data-cy="itemsequence"
                    type="text"
                    value={itemList.length + 1}
                    readOnly
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.isPackage')}
                    id="item-isPackage"
                    name="isPackage"
                    data-cy="isPackage"
                    onChange={handleItemChange}
                    check
                    type="checkbox"
                  />
                  <ValidatedField label={translate('hcpNphiesPortalApp.item.tax')} id="item-tax" name="tax" data-cy="tax" type="text" />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.payerShare')}
                    id="item-payerShare"
                    name="payerShare"
                    data-cy="payerShare"
                    onChange={handleItemChange}
                    type="text"
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.patientShare')}
                    id="item-patientShare"
                    name="patientShare"
                    data-cy="patientShare"
                    type="text"
                    onChange={handleItemChange}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.careTeamSequence')}
                    id="item-careTeamSequences"
                    name="careTeamSequences"
                    data-cy="careTeamSequences"
                    max={careTeamList.length}
                    type="number"
                    onChange={handleItemChange}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                  />
                  <Label>Diagnosis Sequence</Label>
                  {diagnosisList.map((i, index) => (
                    <>
                      <p style={{ paddingLeft: '20px' }}>
                        <Input
                          type="checkbox"
                          checked={itemDiagList.map(x => x.diagSeq).indexOf(i.sequence) > -1}
                          onChange={handleItemDiagChange}
                          label={i.sequence}
                          value={i.sequence}
                          name={'diag' + i.sequence}
                        />
                        {i.sequence}
                      </p>
                    </>
                  ))}
                  <br />
                  <Label>Supporting Info Sequence</Label>
                  {supportingInfoList.map((i, index) => (
                    <>
                      <p style={{ paddingLeft: '20px' }}>
                        <Input
                          type="checkbox"
                          checked={itemSuppList.map(x => x.infSeq).indexOf(i.sequence) > -1}
                          onChange={handleItemSuppChange}
                          label={i.sequence}
                          value={i.seqeunce}
                          name={'supp' + i.sequence}
                        />
                        {i.sequence}
                      </p>
                    </>
                  ))}
                  <br />
                  <ValidatedField label="Product or service category" name="productType" type="select" onChange={changeProductType}>
                    <option value="0"></option>
                    <option value="Imaging">Imaging</option>
                    <option value="Laboratory">Laboratory</option>
                    <option value="MedicalDevices">Medical Devices</option>
                    <option value="MedicationCodes">Medication</option>
                    <option value="OralHealthIP">Oral Health IP</option>
                    <option value="OralHealthOP">Oral Health OP</option>
                    <option value="Procedures">Procedures</option>
                    <option value="Services">Services</option>
                    <option value="Transportation">Transportation</option>
                  </ValidatedField>
                  <FormGroup>
                    <Label for="claim-product">Product</Label>
                    <FormGroup>
                      <Autocomplete
                        shouldItemRender={matchProdToTerm}
                        getItemValue={item => getProdTerm(item)}
                        items={[...filterProducts]}
                        renderItem={(item, isHighlighted) => (
                          <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getProdTerm(item)}</div>
                        )}
                        value={productId}
                        onChange={changeProduct}
                        onSelect={selectProduct}
                      />
                    </FormGroup>
                  </FormGroup>
                  <div onChange={changeItemDate}>
                    <input type="radio" value="0" name="itemDate" checked={itemDate} /> Date&nbsp;
                    <input type="radio" value="1" name="itemDate" checked={!itemDate} /> Period&nbsp;
                  </div>
                  {itemDate ? (
                    <ValidatedField
                      label={translate('hcpNphiesPortalApp.item.servicedDate')}
                      id="item-servicedDate"
                      name="servicedDate"
                      data-cy="servicedDate"
                      type="datetime-local"
                      onChange={handleItemChange}
                      placeholder="YYYY-MM-DD HH:mm"
                    />
                  ) : (
                    <>
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.item.servicedDateStart')}
                        id="item-servicedDateStart"
                        name="servicedDateStart"
                        data-cy="servicedDateStart"
                        type="datetime-local"
                        onChange={handleItemChange}
                        placeholder="YYYY-MM-DD HH:mm"
                      />
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.item.servicedDateEnd')}
                        id="item-servicedDateEnd"
                        name="servicedDateEnd"
                        data-cy="servicedDateEnd"
                        type="datetime-local"
                        onChange={handleItemChange}
                        placeholder="YYYY-MM-DD HH:mm"
                      />
                    </>
                  )}
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.quantity')}
                    id="item-quantity"
                    name="quantity"
                    data-cy="quantity"
                    type="text"
                    onChange={handleItemChange}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.unitPrice')}
                    id="item-unitPrice"
                    name="unitPrice"
                    data-cy="unitPrice"
                    type="text"
                    onChange={handleItemChange}
                    validate={{
                      required: { value: true, message: translate('entity.validation.required') },
                      validate: v => isNumber(v) || translate('entity.validation.number'),
                    }}
                  />
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.factor')}
                    id="item-factor"
                    name="factor"
                    data-cy="factor"
                    type="text"
                    onChange={handleItemChange}
                  />
                  <div onChange={changeWithUdi}>
                    Udi&nbsp;&nbsp;
                    <input type="radio" value="1" checked={withUdi} /> Yes&nbsp;
                    <input type="radio" value="0" checked={!withUdi} /> No&nbsp;
                  </div>
                  {withUdi ? (
                    <div style={{ paddingLeft: '30px' }}>
                      <ul>
                        {udiList.map(e => (
                          <li style={{ padding: 10 }} key={e.name}>
                            {getUdiTerm(e)}{' '}
                            <Button style={{ float: 'right' }} color="danger" onClick={() => removeUdi(e.index)}>
                              <Translate contentKey="entity.action.deleteudi">Delete udi</Translate>
                            </Button>
                          </li>
                        ))}
                      </ul>
                      <div onChange={changeWithUdiIdentifier}>
                        <input type="radio" value="1" checked={withUdiIdentifier} /> Identifier&nbsp;
                        <input type="radio" value="0" checked={!withUdiIdentifier} /> Ref&nbsp;
                      </div>
                      {withUdiIdentifier ? (
                        <div style={{ paddingLeft: '30px' }}>
                          <ValidatedField
                            id="udiReference-ref"
                            name="ref"
                            data-cy="ref"
                            label={translate('hcpNphiesPortalApp.item.udi') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.ref')}
                            type="select"
                            onChange={handleUdiChange}
                          >
                            <option value=""></option>
                            <option value="Device" label="Device" key="Device" />
                          </ValidatedField>
                          <ValidatedField
                            label={
                              translate('hcpNphiesPortalApp.item.udi') +
                              ' ' +
                              translate('hcpNphiesPortalApp.referenceIdentifier.identifier')
                            }
                            id="udiReference-identifier"
                            name="udiReference.identifier"
                            data-cy="udiReference.identifier"
                            type="text"
                            onChange={handleUdiChange}
                          />
                        </div>
                      ) : (
                        <FormGroup style={{ paddingLeft: '30px' }}>
                          Device Form
                          <ValidatedField
                            label={translate('hcpNphiesPortalApp.item.udi') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.id')}
                            id="udiReference-id"
                            name="udiReference.id"
                            data-cy="udiReference.id"
                            type="text"
                            onChange={handleUdiChange}
                          />
                        </FormGroup>
                      )}
                      <FormGroup>
                        <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleUdiSubmit}>
                          Add Udi
                        </Button>
                      </FormGroup>
                    </div>
                  ) : null}
                  <div onChange={changeWithDetail}>
                    Details&nbsp;&nbsp;
                    <input type="radio" value="1" checked={withDetail} /> Yes&nbsp;
                    <input type="radio" value="0" checked={!withDetail} /> No&nbsp;
                  </div>
                  {withDetail ? (
                    <div style={{ paddingLeft: '30px' }}>
                      <>
                        <Button style={{ float: 'right' }} color="danger" onClick={() => removeDetailItem()}>
                          <Translate contentKey="entity.action.deletelatest">Delete detail</Translate>
                        </Button>
                        <ul>
                          {detailItemList.map(e => (
                            <li style={{ padding: 10 }} key={e.name}>
                              {getItemTerm(e)}{' '}
                            </li>
                          ))}
                        </ul>
                      </>
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.detailItem.sequence')}
                        id="detail-item-sequence"
                        name="detsequence"
                        data-cy="detsequence"
                        type="text"
                        value={detailItemList.length + 1}
                        readOnly
                      />
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.detailItem.tax')}
                        id="detail-item-tax"
                        name="tax"
                        data-cy="tax"
                        type="text"
                        onChange={handleDetailItemChange}
                      />
                      <ValidatedField
                        label="Product or service category"
                        name="productDetailType"
                        type="select"
                        onChange={changeProductDetailType}
                      >
                        <option value="0"></option>
                        <option value="Imaging">Imaging</option>
                        <option value="Laboratory">Laboratory</option>
                        <option value="MedicalDevices">Medical Devices</option>
                        <option value="MedicationCodes">Medication</option>
                        <option value="OralHealthIP">Oral Health IP</option>
                        <option value="OralHealthOP">Oral Health OP</option>
                        <option value="Procedures">Procedures</option>
                        <option value="Services">Services</option>
                        <option value="Transportation">Transportation</option>
                      </ValidatedField>
                      <FormGroup>
                        <Label for="claim-product">Product</Label>
                        <FormGroup>
                          <Autocomplete
                            shouldItemRender={matchProdToTerm}
                            getItemValue={item => getProdTerm(item)}
                            items={[...filterDetailProducts]}
                            renderItem={(item, isHighlighted) => (
                              <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getProdTerm(item)}</div>
                            )}
                            value={productDetailId}
                            onChange={changeProductDetail}
                            onSelect={selectProductDetail}
                          />
                        </FormGroup>
                      </FormGroup>
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.detailItem.quantity')}
                        id="detail-item-quantity"
                        name="quantity"
                        data-cy="quantity"
                        type="text"
                        onChange={handleDetailItemChange}
                        validate={{
                          required: { value: true, message: translate('entity.validation.required') },
                          validate: v => isNumber(v) || translate('entity.validation.number'),
                        }}
                      />
                      <ValidatedField
                        label={translate('hcpNphiesPortalApp.detailItem.unitPrice')}
                        id="detail-item-unitPrice"
                        name="unitPrice"
                        data-cy="unitPrice"
                        type="text"
                        onChange={handleDetailItemChange}
                        validate={{
                          required: { value: true, message: translate('entity.validation.required') },
                          validate: v => isNumber(v) || translate('entity.validation.number'),
                        }}
                      />
                      <div onChange={changeWithDetailUdi}>
                        Udi&nbsp;&nbsp;
                        <input type="radio" value="1" checked={withDetailUdi} /> Yes&nbsp;
                        <input type="radio" value="0" checked={!withDetailUdi} /> No&nbsp;
                      </div>
                      {withDetailUdi ? (
                        <div style={{ paddingLeft: '30px' }}>
                          <ul>
                            {detailUdiList.map(e => (
                              <li style={{ padding: 10 }} key={e.name}>
                                {getUdiTerm(e)}{' '}
                                <Button style={{ float: 'right' }} color="danger" onClick={() => removeDetailUdi(e.index)}>
                                  <Translate contentKey="entity.action.deleteudi">Delete udi</Translate>
                                </Button>
                              </li>
                            ))}
                          </ul>
                          <div onChange={changeWithDetailUdiIdentifier}>
                            <input type="radio" value="1" checked={withDetailUdiIdentifier} /> Identifier&nbsp;
                            <input type="radio" value="0" checked={!withDetailUdiIdentifier} /> Ref&nbsp;
                          </div>
                          {withDetailUdiIdentifier ? (
                            <div style={{ paddingLeft: '30px' }}>
                              <ValidatedField
                                id="detailUdiReference-ref"
                                name="detailUdiReference.ref"
                                data-cy="detailUdiReference.ref"
                                label={
                                  translate('hcpNphiesPortalApp.item.udi') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.ref')
                                }
                                type="select"
                                onChange={handleDetailUdiChange}
                              >
                                <option value=""></option>
                                <option value="Device" label="Device" key="Device" />
                              </ValidatedField>
                              <ValidatedField
                                label={
                                  translate('hcpNphiesPortalApp.item.udi') +
                                  ' ' +
                                  translate('hcpNphiesPortalApp.referenceIdentifier.identifier')
                                }
                                id="detailUdiReference-identifier"
                                name="detailUdiReference.identifier"
                                data-cy="detailUdiReference.identifier"
                                type="text"
                                onChange={handleDetailUdiChange}
                              />
                            </div>
                          ) : (
                            <FormGroup style={{ paddingLeft: '30px' }}>
                              Device Form
                              <ValidatedField
                                label={
                                  translate('hcpNphiesPortalApp.item.Udi') + ' ' + translate('hcpNphiesPortalApp.referenceIdentifier.id')
                                }
                                id="detailUdiReference-id"
                                name="detailUdiReference.id"
                                data-cy="detailUdiReference.id"
                                type="text"
                                onChange={handleDetailUdiChange}
                              />
                            </FormGroup>
                          )}
                          <FormGroup>
                            <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleDetailUdiSubmit}>
                              Add Udi
                            </Button>
                          </FormGroup>
                        </div>
                      ) : null}
                      <div onChange={changeWithSubDetail}>
                        Subdetails&nbsp;&nbsp;
                        <input type="radio" value="1" checked={withSubDetail} /> Yes&nbsp;
                        <input type="radio" value="0" checked={!withSubDetail} /> No&nbsp;
                      </div>
                      {withSubDetail ? (
                        <div style={{ paddingLeft: '30px' }}>
                          <>
                            <Button style={{ float: 'right' }} color="danger" onClick={() => removeSubDetailItem()}>
                              <Translate contentKey="entity.action.deletelatest">Delete subDetail</Translate>
                            </Button>
                            <ul>
                              {subDetailItemList.map(e => (
                                <li style={{ padding: 10 }} key={e.name}>
                                  {getItemTerm(e)}{' '}
                                </li>
                              ))}
                            </ul>
                          </>
                          <ValidatedField
                            label={translate('hcpNphiesPortalApp.subDetailItem.sequence')}
                            id="subDetail-item-sequence"
                            name="subsequence"
                            data-cy="subsequence"
                            type="text"
                            value={subDetailItemList.length + 1}
                            readOnly
                          />
                          <ValidatedField
                            label={translate('hcpNphiesPortalApp.subDetailItem.tax')}
                            id="subDetail-item-tax"
                            name="tax"
                            data-cy="tax"
                            type="text"
                            onChange={handleSubDetailItemChange}
                          />
                          <ValidatedField
                            label="Product or service category"
                            name="productSubDetailType"
                            type="select"
                            onChange={changeProductSubDetailType}
                          >
                            <option value="0"></option>
                            <option value="Imaging">Imaging</option>
                            <option value="Laboratory">Laboratory</option>
                            <option value="MedicalDevices">Medical Devices</option>
                            <option value="MedicationCodes">Medication</option>
                            <option value="OralHealthIP">Oral Health IP</option>
                            <option value="OralHealthOP">Oral Health OP</option>
                            <option value="Procedures">Procedures</option>
                            <option value="Services">Services</option>
                            <option value="Transportation">Transportation</option>
                          </ValidatedField>
                          <FormGroup>
                            <Label for="claim-product">Product</Label>
                            <FormGroup>
                              <Autocomplete
                                shouldItemRender={matchProdToTerm}
                                getItemValue={item => getProdTerm(item)}
                                items={[...filterSubDetailProducts]}
                                renderItem={(item, isHighlighted) => (
                                  <div style={{ background: isHighlighted ? 'lightgray' : 'white' }}>{getProdTerm(item)}</div>
                                )}
                                value={productSubDetailId}
                                onChange={changeProductSubDetail}
                                onSelect={selectProductSubDetail}
                              />
                            </FormGroup>
                          </FormGroup>
                          <ValidatedField
                            label={translate('hcpNphiesPortalApp.subDetailItem.quantity')}
                            id="subDetail-item-quantity"
                            name="quantity"
                            data-cy="quantity"
                            type="text"
                            onChange={handleSubDetailItemChange}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                              validate: v => isNumber(v) || translate('entity.validation.number'),
                            }}
                          />
                          <ValidatedField
                            label={translate('hcpNphiesPortalApp.subDetailItem.unitPrice')}
                            id="subDetail-item-unitPrice"
                            name="unitPrice"
                            data-cy="unitPrice"
                            type="text"
                            onChange={handleSubDetailItemChange}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                              validate: v => isNumber(v) || translate('entity.validation.number'),
                            }}
                          />
                          <div onChange={changeWithSubDetailUdi}>
                            Udi&nbsp;&nbsp;
                            <input type="radio" value="1" checked={withSubDetailUdi} /> Yes&nbsp;
                            <input type="radio" value="0" checked={!withSubDetailUdi} /> No&nbsp;
                          </div>
                          {withSubDetailUdi ? (
                            <div style={{ paddingLeft: '30px' }}>
                              <ul>
                                {subDetailUdiList.map(e => (
                                  <li style={{ padding: 10 }} key={e.name}>
                                    {getUdiTerm(e)}{' '}
                                    <Button style={{ float: 'right' }} color="danger" onClick={() => removeSubDetailUdi(e.index)}>
                                      <Translate contentKey="entity.action.deleteudi">Delete subDetail Udi</Translate>
                                    </Button>
                                  </li>
                                ))}
                              </ul>
                              <div onChange={changeWithSubDetailUdiIdentifier}>
                                <input type="radio" value="1" checked={withSubDetailUdiIdentifier} /> Identifier&nbsp;
                                <input type="radio" value="0" checked={!withSubDetailUdiIdentifier} /> Ref&nbsp;
                              </div>
                              {withSubDetailUdiIdentifier ? (
                                <div style={{ paddingLeft: '30px' }}>
                                  <ValidatedField
                                    id="subDetailUdiReference-ref"
                                    name="ref"
                                    data-cy="ref"
                                    label={
                                      translate('hcpNphiesPortalApp.item.subDetailUdi') +
                                      ' ' +
                                      translate('hcpNphiesPortalApp.referenceIdentifier.ref')
                                    }
                                    type="select"
                                    onChange={handleSubDetailUdiChange}
                                  >
                                    <option value=""></option>
                                    <option value="Device" label="Device" key="Device" />
                                  </ValidatedField>
                                  <ValidatedField
                                    label={
                                      translate('hcpNphiesPortalApp.item.udi') +
                                      ' ' +
                                      translate('hcpNphiesPortalApp.referenceIdentifier.identifier')
                                    }
                                    id="subDetailUdiReference-identifier"
                                    name="identifier"
                                    data-cy="identifier"
                                    type="text"
                                    onChange={handleSubDetailUdiChange}
                                  />
                                </div>
                              ) : (
                                <FormGroup style={{ paddingLeft: '30px' }}>
                                  Device Form
                                  <ValidatedField
                                    label={
                                      translate('hcpNphiesPortalApp.item.udi') +
                                      ' ' +
                                      translate('hcpNphiesPortalApp.referenceIdentifier.id')
                                    }
                                    id="subDetailUdiReference-id"
                                    name="id"
                                    data-cy="id"
                                    type="text"
                                    onChange={handleSubDetailUdiChange}
                                  />
                                </FormGroup>
                              )}
                              <FormGroup>
                                <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleSubDetailUdiSubmit}>
                                  Add Udi
                                </Button>
                              </FormGroup>
                            </div>
                          ) : null}
                          <FormGroup>
                            <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleSubDetailItemSubmit}>
                              Add subdetail
                            </Button>
                          </FormGroup>
                        </div>
                      ) : null}
                      <FormGroup>
                        <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleDetailItemSubmit}>
                          Add detail
                        </Button>
                      </FormGroup>
                    </div>
                  ) : null}
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.bodySite')}
                    id="item-bodySite"
                    name="bodySite"
                    data-cy="bodySite"
                    type="select"
                    onChange={handleItemChange}
                  >
                    <option value=""></option>
                    <option value="E1">{translate('hcpNphiesPortalApp.BodySiteEnum.E1')}</option>
                    <option value="E2">{translate('hcpNphiesPortalApp.BodySiteEnum.E2')}</option>
                    <option value="E3">{translate('hcpNphiesPortalApp.BodySiteEnum.E3')}</option>
                    <option value="E4">{translate('hcpNphiesPortalApp.BodySiteEnum.E4')}</option>
                    <option value="F1">{translate('hcpNphiesPortalApp.BodySiteEnum.F1')}</option>
                    <option value="F2">{translate('hcpNphiesPortalApp.BodySiteEnum.F2')}</option>
                    <option value="F3">{translate('hcpNphiesPortalApp.BodySiteEnum.F3')}</option>
                    <option value="F4">{translate('hcpNphiesPortalApp.BodySiteEnum.F4')}</option>
                    <option value="F5">{translate('hcpNphiesPortalApp.BodySiteEnum.F5')}</option>
                    <option value="F6">{translate('hcpNphiesPortalApp.BodySiteEnum.F6')}</option>
                    <option value="F7">{translate('hcpNphiesPortalApp.BodySiteEnum.F7')}</option>
                    <option value="F8">{translate('hcpNphiesPortalApp.BodySiteEnum.F8')}</option>
                    <option value="F9">{translate('hcpNphiesPortalApp.BodySiteEnum.F9')}</option>
                    <option value="FA">{translate('hcpNphiesPortalApp.BodySiteEnum.FA')}</option>
                    <option value="LC">{translate('hcpNphiesPortalApp.BodySiteEnum.LC')}</option>
                    <option value="LD">{translate('hcpNphiesPortalApp.BodySiteEnum.LD')}</option>
                    <option value="LM">{translate('hcpNphiesPortalApp.BodySiteEnum.LM')}</option>
                    <option value="LT">{translate('hcpNphiesPortalApp.BodySiteEnum.LT')}</option>
                    <option value="RC">{translate('hcpNphiesPortalApp.BodySiteEnum.RC')}</option>
                    <option value="RI">{translate('hcpNphiesPortalApp.BodySiteEnum.RI')}</option>
                    <option value="RT">{translate('hcpNphiesPortalApp.BodySiteEnum.RT')}</option>
                    <option value="T1">{translate('hcpNphiesPortalApp.BodySiteEnum.T1')}</option>
                    <option value="T2">{translate('hcpNphiesPortalApp.BodySiteEnum.T2')}</option>
                    <option value="T3">{translate('hcpNphiesPortalApp.BodySiteEnum.T3')}</option>
                    <option value="T4">{translate('hcpNphiesPortalApp.BodySiteEnum.T4')}</option>
                    <option value="T5">{translate('hcpNphiesPortalApp.BodySiteEnum.T5')}</option>
                    <option value="T6">{translate('hcpNphiesPortalApp.BodySiteEnum.T6')}</option>
                    <option value="T7">{translate('hcpNphiesPortalApp.BodySiteEnum.T7')}</option>
                    <option value="T8">{translate('hcpNphiesPortalApp.BodySiteEnum.T8')}</option>
                    <option value="T9">{translate('hcpNphiesPortalApp.BodySiteEnum.T9')}</option>
                    <option value="TA">{translate('hcpNphiesPortalApp.BodySiteEnum.TA')}</option>
                  </ValidatedField>
                  <ValidatedField
                    label={translate('hcpNphiesPortalApp.item.subSite')}
                    id="item-subSite"
                    name="subSite"
                    data-cy="subSite"
                    type="select"
                    onChange={handleItemChange}
                  >
                    <option value=""></option>
                    <option value="R">{translate('hcpNphiesPortalApp.SubSiteEnum.R')}</option>
                    <option value="L">{translate('hcpNphiesPortalApp.SubSiteEnum.L')}</option>
                    <option value="U">{translate('hcpNphiesPortalApp.SubSiteEnum.U')}</option>
                    <option value="D">{translate('hcpNphiesPortalApp.SubSiteEnum.D')}</option>
                    <option value="A">{translate('hcpNphiesPortalApp.SubSiteEnum.A')}</option>
                    <option value="P">{translate('hcpNphiesPortalApp.SubSiteEnum.P')}</option>
                    <option value="I">{translate('hcpNphiesPortalApp.SubSiteEnum.I')}</option>
                    <option value="E">{translate('hcpNphiesPortalApp.SubSiteEnum.E')}</option>
                  </ValidatedField>
                  <FormGroup>
                    <Button color="info" onClick={toggleItem}>
                      <Translate contentKey="entity.action.back">Back</Translate>
                    </Button>
                    &nbsp;
                    <Button color="primary" id="add-name" data-cy="entityCreateSaveButton" onClick={handleItemSubmit}>
                      <Translate contentKey="entity.action.save">Save</Translate>
                    </Button>
                  </FormGroup>
                </Form>
              </MyModal>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/claim" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ClaimUpdate;
