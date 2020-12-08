<template>
    <ul :class="$style.list">
        <li v-for="element in data">
            <div class="row">
                <div class="col-10">
                    <p :class="$style.title">{{ element.course.title }}</p>
                    <p :class="$style.counter">{{ element.lessons.length }} teachers available</p>
                </div>
                <div :class="$style.controls + ' col-2'">
                    <a class="button" href="javascript:void(0);" v-on:click="toggleDetails">Details</a>
                </div>
            </div>
            <div :class="$style.details" data-open="false">
                <ul :class="$style.lessons">
                    <li v-for="lesson in element.lessons">
                        <p>{{ lesson.teacher.name + ' ' + lesson.teacher.surname }}</p>
                        <ul :class="$style.slots">
                            <li v-for="slot in lesson.slots" :data-available="slot.available" :data-mine="slot.mine" :data-day="slot.day" :data-hour="slot.hour" :data-tid="lesson.teacher.id" :data-cid="element.course.id" v-on:click="bookLesson">
                                <p>{{ getDayName(slot.day) }}</p>
                                <p :class="$style.hour">{{ slot.hour + ':00 - ' + ( slot.hour + 1 ) + ':00' }}</p>
                            </li>
                        </ul>
                    </li>
                </ul>
                <p class="mt-2">Click on any available slot to book a lesson, click on red marked lessons to cancel your reservation.</p>
            </div>
        </li>
    </ul>
</template>
<script src="./available-lesson-list.js"></script>
<style lang="sass" src="./available-lesson-list.scss" module></style>
