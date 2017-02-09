package com.places.model;

import com.google.android.gms.maps.model.LatLng;
import com.places.model.PlacesRepository;
import com.places.model.data.Address;
import com.places.model.data.Addresses;
import com.places.model.remote.RemotePlacesDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.Assert.assertEquals;



/**
 * @author Gilad Opher
 */
public class PlacesRepositoryTest{

	private LatLng LOCATION = new LatLng(31.881905, 34.803593);
	private LatLng OTHER_LOCATION = new LatLng(11.881905, 22.803593);

	private static Address address1 = new Address("address1");
	private static Address address2 = new Address("address2");
	private static Address address3 = new Address("address3");

	private static List<Address> addresses_full = Arrays.asList(address1, address2, address3);
	private static Addresses ADDRESSES_FULL = new Addresses(addresses_full);
	private static Addresses ADDRESSES_EMPTY = new Addresses(new ArrayList<Address>());


	@Mock
	private RemotePlacesDataSource remotePlacesDataSource;


	private PlacesRepository placesRepository;

	@Before
	public void setupNotesPresenter() {

		MockitoAnnotations.initMocks(this);

		// Get a reference to the class under test
		placesRepository = new PlacesRepository(remotePlacesDataSource);
	}

	@Test
	public void verifyValidAddress(){
		assertEquals(placesRepository.validAddress(ADDRESSES_FULL), true);
	}

	@Test
	public void verifyInvalidAddress(){
		assertEquals(placesRepository.validAddress(null), false);
	}

	@Test
	public void verifyEmptyAddress(){
		assertEquals(placesRepository.validAddress(ADDRESSES_EMPTY), false);
	}




}
