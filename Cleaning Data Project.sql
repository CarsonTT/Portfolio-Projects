SELECT * FROM `nashvile housing`.nashville_housing;

-- ---------------------------------------------------------------------------

-- Standrize Date Format


Alter Table nashville_housing
Modify SaleDate date;


-- ---------------------------------------------------------------------------

-- Populate Property Address Data


select *
from nashville_housing
-- where PropertyAddress is null
order by ParcelID;


SELECT 
	b.PropertyAddress as PropertyAddress2, 
	ifnull(a.propertyaddress,b.propertyaddress)
FROM nashville_housing a		
join nashville_housing b		on a.ParcelID = b.ParcelID
								and a.UniqueID != b.UniqueID
join nashville_housing b		on a.ParcelID = b.ParcelID
								and a.UniqueID != b.UniqueID
where a.PropertyAddress is null;


-- TOOK ME WAY TOO LONG TO FIGURE OUT HOW THIS WORKED
update nashville_housing a
join  nashville_housing b		on a.ParcelID = b.ParcelID
								and a.UniqueID != b.UniqueID
join  nashville_housing b		on a.ParcelID = b.ParcelID
								and a.UniqueID != b.UniqueID
set a.propertyaddress = ifnull(a.propertyaddress,b.propertyaddress)
where a.PropertyAddress is null;
    

-- ---------------------------------------------------------------------------

-- Breaking out Property Address Into Individual Columns (Address, City)


SELECT PropertyAddress
FROM nashville_housing;


SELECT 
	substr(propertyaddress, 1, locate(',',propertyaddress) -1 ) as Address,
    substr(propertyaddress, locate(',',propertyaddress) +1, char_length(propertyaddress)) as city
FROM nashville_housing;


Alter Table nashville_housing
ADD PropertySplitAddress nvarchar(255);

update nashville_housing
set PropertySplitAddress = substr(propertyaddress, 1, locate(',',propertyaddress) -1 );


Alter Table nashville_housing
ADD PropertySplitCity NVARCHAR(255);

update nashville_housing
set PropertySplitCity = substr(propertyaddress, locate(',',propertyaddress) +1, char_length(propertyaddress));





-- Breaking out Owner Address Into Individual Columns (Address, City, State)


select OwnerAddress
from nashville_housing; 


Select	
	OwnerAddress,
	substring_index(OwnerAddress, ',', 1) as Address,
    substring_index(substring_index(OwnerAddress, ',', 2), ',', -1) as City,
    substring_index(OwnerAddress, ',', -1) as State
from nashville_housing;


Alter Table nashville_housing
ADD OwnerSplitAddress nvarchar(255);

update nashville_housing
set OwnerSplitAddress = substring_index(OwnerAddress, ',', 1);


Alter Table nashville_housing
ADD OwnerSplitCity nvarchar(255);

update nashville_housing
set OwnerSplitCity = substring_index(substring_index(OwnerAddress, ',', 2), ',', -1);


Alter Table nashville_housing
ADD OwnerSplitState nvarchar(255);

update nashville_housing
set OwnerSplitState = substring_index(OwnerAddress, ',', -1);

-- (FYI to improve effiency create all new cloumns first then update all the columns)




-- Change Y and N to Yes and No in "Sold as Vacant" Field


Select distinct
	(soldasvacant),
    Count(soldasvacant)
from nashville_housing
group by SoldAsVacant
order by 2;


Select 
	SoldasVacant,
    Case 
		When SoldAsVacant = 'Y' Then 'Yes'
		when SoldAsVacant = 'N' Then 'No'
		Else SoldAsVacant 
    End as Changes
From nashville_housing
order by 1;


Update nashville_housing
Set SoldAsVacant = 
	Case 
		When SoldAsVacant = 'Y' Then 'Yes'
		when SoldAsVacant = 'N' Then 'No'
		Else SoldAsVacant 
	end;
    
    
-- ---------------------------------------------------------------------------

    
-- Remove Duplicates


-- Shows where duplicates are in table

Select *,
	row_number() OVER (
    Partition by ParcelID,
				PropertyAddress,
                SalePrice,
                saleDate,
                LegalReference
	order by uniqueID) row_num 
from nashville_housing;


-- Shows ONLY Duplicates

Select *
from (
		Select UniqueID,
			row_number() OVER (
    Partition by ParcelID,
				PropertyAddress,
                SalePrice,
                saleDate,
                LegalReference
	order by uniqueID) row_num 
from nashville_housing
	) as temp_table
where row_num > 1;


-- To Delete Duplicates

Delete From nashville_housing
where UniqueID in
(Select	UniqueID
from	(Select	uniqueID,
				row_number() 
                OVER (Partition by 	ParcelID,
									PropertyAddress,
									SalePrice,
									saleDate,
									LegalReference
					order by uniqueID) as row_num 
		from nashville_housing
) as temp_table
where row_num>1
);
	
    
-- --------------------------------------------------------------------------- 


-- Delete UnusedColumns


Select 	* 
from nashville_housing;

Alter Table nashville_housing
drop column Owneraddress, 
drop column PropertyAddress, 
drop column TaxDistrict


-- --------------------------------------------------------------------------- 