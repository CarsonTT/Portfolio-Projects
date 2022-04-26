-- How many people infected in their country
select
	location, population, 
    max(total_cases) as highest_infection_count,
    max((total_cases/population))*100 as percent_population_infected
from covid_deaths
group by location
order by percent_population_infected DESC


-- Total Cases vs Population
SELECT 
	location, date, total_cases, new_cases, total_deaths, 
    (total_cases/population)*100 as percent_population
FROM covid.covid_deaths
where location like '%states%'
order by 1,2


-- Total Cases vs Total Deaths
SELECT 
	location, date, total_cases, new_cases, total_deaths, 
    (total_deaths/total_cases)*100 as death_percentage
FROM covid.covid_deaths
where location like '%states%'
order by 1,2


-- Highest death count per population
select
	location, 
    max(total_deaths) as total_death_count
    from covid_deaths
where continent is not null
group by location
order by total_death_count DESC


-- Death Count in each Continent
SELECT
	continent, 
    Max(total_deaths) as total_death_count
from covid_deaths
where continent is not null
group by continent
order by total_death_count DESC


-- Highest Death per population
Select
	continent, 
    max(total_deaths) as total_death_count
from covid_deaths
where continent is not null
group by continent
order by total_death_count DESC


-- global numbers
SELECT 
	date,
    sum(new_cases) as total_cases, 
    sum(new_deaths) as total_deaths,
    (Sum(new_deaths)/sum(new_cases)) as death_precentage
from covid_deaths
where continent is not null
group by date
order by 1,2


-- total population vs vaccinations
with popvsvac (continent, location,date,population,new_vaccinations,rolling_people_vaccinated) 
as
(
SELECT 
	dea.continent, dea.location, dea.date, dea.population, vac.new_vaccinations,
    sum(vac.new_vaccinations) over (partition by dea.location order by dea.location,dea.date) as rolling_people_vaccinated
from covid_deaths dea
join covid_vaccinations vac
	on dea.location = vac.location
    and dea.date = vac.date
where dea.continent is not null 
order by 2,3
)
select*, (rolling_people_vaccinated/population)*100
from popvsvac


-- temp table
drop table if exists percent_population_vaccinated;
create table percent_population_vaccinated
(
Continent text(255),
location text(255),
date datetime,
population bigint,
new_vaccinations bigint,
rolling_people_vaccinated bigint
);

insert into percent_population_vaccinated
SELECT 
	dea.continent, dea.location, dea.date, dea.population, vac.new_vaccinations,
    sum(vac.new_vaccinations) over (partition by dea.location order by dea.location,dea.date) as rolling_people_vaccinated
from covid_deaths dea
join covid_vaccinations vac
	on dea.location = vac.location
    and dea.date = vac.date
where dea.continent is not null;

select*, (rolling_people_vaccinated/population)*100
from percent_population_vaccinated;


-- view of percent_population_vaccinated
create view percent_population_vaccinated as
SELECT 
	dea.continent, dea.location, dea.date, dea.population, vac.new_vaccinations,
    sum(vac.new_vaccinations) over (partition by dea.location order by dea.location,dea.date) as rolling_people_vaccinated
from covid_deaths dea
join covid_vaccinations vac
	on dea.location = vac.location
    and dea.date = vac.date
where dea.continent is not null

